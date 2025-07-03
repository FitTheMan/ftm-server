package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.UpdateUserCommand;
import com.ftm.server.application.port.in.user.UpdateUserInfoUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadUserImagePort;
import com.ftm.server.application.port.out.persistence.user.LoadUserPort;
import com.ftm.server.application.port.out.persistence.user.UpdateUserImagePort;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3UserImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.user.UserWithImageVo;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserInfoService implements UpdateUserInfoUseCase {

    private final LoadUserImagePort loadUserImagePort;
    private final LoadUserPort loadUserPort;

    private final UpdateUserPort updateUserPort;
    private final UpdateUserImagePort updateUserImagePort;

    private final S3UserImageUploadPort s3UserImageUploadPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    private final AfterCommitExecutorPort afterCommitExecutorPort;
    private final AfterRollbackExecutorPort afterRollbackExecutorPort;

    @Override
    @Transactional
    public UserWithImageVo execute(UpdateUserCommand updateUserCommand) {
        Long userId = updateUserCommand.getUserId();

        User user = loadUserPort.loadUserById(FindByUserIdQuery.of(userId));
        UserImage userImage = loadUserImagePort.loadUserImageByUserId(FindByUserIdQuery.of(userId));

        if (updateUserCommand.getNickname() != null) {
            user.updateUserNickname(updateUserCommand.getNickname());
        }
        if (updateUserCommand.getAgeGroup() != null) {
            user.updateAge(updateUserCommand.getAgeGroup());
        }
        if (updateUserCommand.getHashtags() != null) {
            user.updateHashtag(updateUserCommand.getHashtags());
        }

        // 이미지 업로드 및 기존 이미지 삭제
        if (updateUserCommand.getImageAction() != null
                && updateUserCommand.getImageAction().equals("UPLOAD")
                && updateUserCommand.getProfileImage() != null
                && !updateUserCommand.getProfileImage().isEmpty()) {

            String oldUserImage = userImage.getObjectKey();
            String objectKey =
                    s3UserImageUploadPort.uploadImage(updateUserCommand.getProfileImage());
            userImage.updateUserImage(objectKey);

            // 기존에 존재하던 이미지가 있으면 삭제
            if (!oldUserImage.equals(PropertiesHolder.USER_DEFAULT_IMAGE)) {
                afterCommitExecutorPort.doAfterCommit(
                        () -> s3ImageDeletePort.deleteImage(oldUserImage));
            }

            // transaction rollback 이후 s3에 올라간 이미지 삭제
            afterRollbackExecutorPort.doAfterRollback(
                    () -> s3ImageDeletePort.deleteImage(objectKey));
        } else if (updateUserCommand.getImageAction() != null
                && updateUserCommand.getImageAction().equals("DELETE")) {

            String oldUserImage = userImage.getObjectKey();

            if (!oldUserImage.equals(PropertiesHolder.USER_DEFAULT_IMAGE)) {
                userImage.updateDefaultUserImage();
                afterCommitExecutorPort.doAfterCommit( // transaction commit 이후에 s3에서 이미지 삭제
                        () -> s3ImageDeletePort.deleteImage(oldUserImage));
            }
        }

        updateUserPort.updateUser(user);
        updateUserImagePort.updateUserImage(userImage);

        return UserWithImageVo.of(user, userImage);
    }
}
