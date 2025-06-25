package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.DeleteAllUserByIdListCommand;
import com.ftm.server.application.command.user.DeleteBookmarkByUserIdCommand;
import com.ftm.server.application.command.user.DeleteGroomingTestResultByUserIdCommand;
import com.ftm.server.application.command.user.DeleteUserImageByUserIdCommand;
import com.ftm.server.application.port.in.user.UserHardDeleteUseCase;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.query.FindByUserIdsQuery;
import com.ftm.server.application.query.FindUserByDeleteOptionQuery;
import com.ftm.server.application.query.FindUserByRoleQuery;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.UserRole;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserHardDeleteService implements UserHardDeleteUseCase {

    private final LoadUserPort loadUserPort;
    private final LoadPostPort loadPostPort;

    private final DeleteBookmarkPort deleteBookmarkPort;
    private final DeleteGroomingTestResultPort deleteGroomingTestResultPort;
    private final DeleteUserImagePort deleteUserImagePort;
    private final DeleteUserPort deleteUserPort;

    private final AfterCommitExecutorPort afterCommitExecutorPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    private final UpdatePostUserDomainPort updatePostPort;

    @Override
    @Transactional
    public void execute() {

        // 삭제 대상 user 조회 : 이미 삭제가 된 회원 중 30일이 지난 경우 hard delete 대상
        List<Long> deletedUserIdList =
                loadUserPort
                        .loadUserByDeleteOption(
                                FindUserByDeleteOptionQuery.of(true, LocalDate.now().minusDays(30)))
                        .stream()
                        .map(User::getId)
                        .toList();

        // user 관련 엔티티 모두 삭제 (batch 삭제 진행)
        // 1. 북마크 삭제
        deleteBookmarkPort.deleteBookmarkByUserList(
                DeleteBookmarkByUserIdCommand.of(deletedUserIdList));
        // 2. 그루밍 결과 삭제
        deleteGroomingTestResultPort.deleteGroomingTestResultByUserList(
                DeleteGroomingTestResultByUserIdCommand.of(deletedUserIdList));
        // 3. user 이미지 삭제
        List<String> imageKeyList =
                deleteUserImagePort.deleteUserImageByUserList(
                        DeleteUserImageByUserIdCommand.of(deletedUserIdList));
        afterCommitExecutorPort.doAfterCommit(
                () ->
                        s3ImageDeletePort.deleteImages(
                                imageKeyList)); // transaction commit 이후에 s3에 이미지 삭제 요청

        // 4. user가 쓴 게시글의 작성자를 익명 사용자로 변경
        List<Post> postList =
                loadPostPort.loadPostListByUsers(FindByUserIdsQuery.of(deletedUserIdList));
        if (!postList.isEmpty()) {
            // 익명 사용자 조회
            User systemUser = loadUserPort.loadUserByRole(FindUserByRoleQuery.of(UserRole.SYSTEM));
            Long systemUserId = systemUser.getId();

            // post update
            postList.forEach(p -> p.updateUserId(systemUserId));
            updatePostPort.updatePostListBySystemUser(postList);
        }

        // 5. user 삭제
        deleteUserPort.deleteAllUserByIdList(DeleteAllUserByIdListCommand.of(deletedUserIdList));
    }
}
