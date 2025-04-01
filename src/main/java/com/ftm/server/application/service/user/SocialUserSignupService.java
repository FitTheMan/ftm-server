package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.SocialUserCreationCommand;
import com.ftm.server.application.command.user.SocialUserSignupCommand;
import com.ftm.server.application.port.in.user.SocialUserSignupUseCase;
import com.ftm.server.application.port.out.persistence.user.CheckUserPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.query.FindBySocialValueQuery;
import com.ftm.server.application.vo.user.SocialUserSignupSummaryVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.utils.RandomNickNameCreator;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialUserSignupService implements SocialUserSignupUseCase {

    private final CheckUserPort checkUserPort;
    private final SaveUserPort saveUserPort;
    private final SaveUserImagePort saveUserImagePort;

    @Transactional
    @Override
    public SocialUserSignupSummaryVo execute(SocialUserSignupCommand command) {

        // 이미 가입된 회원인지 한번 더 확인
        if (checkUserPort.checksUserBySocialValue(
                FindBySocialValueQuery.of(command.getSocialProvider(), command.getSocialId()))) {
            throw new CustomException(ErrorResponseCode.USER_ALREADY_EXISTS);
        }

        String nickname = RandomNickNameCreator.generateNickname();

        SocialUserCreationCommand command1 =
                SocialUserCreationCommand.of(
                        command.getSocialProvider(),
                        command.getSocialId(),
                        nickname,
                        command.getAge(),
                        command.getHashtags());

        User user = saveUserPort.saveSocialUser(User.createSocailUser(command1));
        UserImage userImage = UserImage.createUserImage(user.getId());
        saveUserImagePort.saveUserDefaultImage(userImage);

        return SocialUserSignupSummaryVo.from(user, userImage.getObjectKey(), null);
    }
}
