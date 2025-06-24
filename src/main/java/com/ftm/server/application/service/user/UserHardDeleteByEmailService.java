package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.*;
import com.ftm.server.application.port.in.user.UserHardDeleteByEmailUseCase;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindUserByDeleteOptionQuery;
import com.ftm.server.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserHardDeleteByEmailService implements UserHardDeleteByEmailUseCase {

    private final LoadUserPort loadUserPort;

    private final DeleteBookmarkPort deleteBookmarkPort;
    private final DeleteGroomingTestResultPort deleteGroomingTestResultPort;
    private final DeleteUserImagePort deleteUserImagePort;
    private final DeleteUserPort deleteUserPort;

    private final AfterCommitExecutorPort afterCommitExecutorPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    @Override
    public void execute(DeleteUserByEmailCommand command) {
        // 삭제 대상 user 조회
        Optional<User> deletedUser  =
                loadUserPort.loadDeletedUserByEmail(FindByEmailQuery.of(command.getEmail()));

        if (deletedUser.isEmpty()){return;}

        List<Long> userId = List.of(deletedUser.get().getId());

        // user 관련 엔티티 모두 삭제
        // 1. 북마크 삭제
        deleteBookmarkPort.deleteBookmarkByUserList(
                DeleteBookmarkByUserIdCommand.of(userId));
        // 2. 그루밍 결과 삭제
        deleteGroomingTestResultPort.deleteGroomingTestResultByUserList(
                DeleteGroomingTestResultByUserIdCommand.of(userId));
        // 3. user 이미지 삭제
        List<String> imageKeyList =
                deleteUserImagePort.deleteUserImageByUserList(
                        DeleteUserImageByUserIdCommand.of(userId));
        afterCommitExecutorPort.doAfterCommit(
                () ->
                        s3ImageDeletePort.deleteImages(
                                imageKeyList)); // transaction commit 이후에 s3에 이미지 삭제 요청
        // 4. user 삭제
        deleteUserPort.deleteAllUserByIdList(DeleteAllUserByIdListCommand.of(userId));
    }
}
