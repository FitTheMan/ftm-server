package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.*;
import com.ftm.server.application.port.in.user.UserHardDeleteByEmailUseCase;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindUserByRoleQuery;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.UserRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHardDeleteByEmailService implements UserHardDeleteByEmailUseCase {

    private final LoadUserPort loadUserPort;
    private final LoadPostUserDomainPort loadPostPort;

    private final UpdatePostUserDomainPort updatePostPort;

    private final DeleteBookmarkPort deleteBookmarkPort;
    private final DeleteGroomingTestResultPort deleteGroomingTestResultPort;
    private final DeleteUserImagePort deleteUserImagePort;
    private final DeleteUserPort deleteUserPort;

    private final AfterCommitExecutorPort afterCommitExecutorPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    @Override
    public void execute(DeleteUserByEmailCommand command) {
        // 삭제 대상 user 조회
        Optional<User> deletedUser =
                loadUserPort.loadDeletedUserByEmail(FindByEmailQuery.of(command.getEmail()));

        if (deletedUser.isEmpty()) {
            return;
        }

        Long userId = deletedUser.get().getId();
        List<Long> userIds = List.of(userId);

        // user 관련 엔티티 모두 삭제
        // 1. 북마크 삭제
        deleteBookmarkPort.deleteBookmarkByUserList(DeleteBookmarkByUserIdCommand.of(userIds));
        // 2. 그루밍 결과 삭제
        deleteGroomingTestResultPort.deleteGroomingTestResultByUserList(
                DeleteGroomingTestResultByUserIdCommand.of(userIds));
        // 3. user 이미지 삭제
        List<String> imageKeyList =
                deleteUserImagePort.deleteUserImageByUserList(
                        DeleteUserImageByUserIdCommand.of(userIds));
        afterCommitExecutorPort.doAfterCommit(
                () ->
                        s3ImageDeletePort.deleteImages(
                                imageKeyList)); // transaction commit 이후에 s3에 이미지 삭제 요청

        // 4. user가 쓴 게시글의 작성자를 익명 사용자로 변경
        List<Post> postList = loadPostPort.loadPostListByUser(FindByUserIdQuery.of(userId));
        if (!postList.isEmpty()) {
            // 익명 사용자 조회
            User systemUser = loadUserPort.loadUserByRole(FindUserByRoleQuery.of(UserRole.SYSTEM));
            Long systemUserId = systemUser.getId();
            // post update
            postList.forEach(p -> p.updateUserId(systemUserId));
            updatePostPort.updatePostListBySystemUser(postList);
        }

        // 5. user 삭제
        deleteUserPort.deleteAllUserByIdList(DeleteAllUserByIdListCommand.of(userIds));
    }
}
