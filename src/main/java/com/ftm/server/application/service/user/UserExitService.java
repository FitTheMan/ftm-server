package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.DeleteUserByIdCommand;
import com.ftm.server.application.port.in.user.UserExitUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadPostUserDomainPort;
import com.ftm.server.application.port.out.persistence.user.LoadUserPort;
import com.ftm.server.application.port.out.persistence.user.UpdatePostUserDomainPort;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindUserByRoleQuery;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.UserRole;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserExitService implements UserExitUseCase {

    private final LoadUserPort loadUserPort;
    private final LoadPostUserDomainPort loadPostPort;

    private final UpdateUserPort updateUserPort;
    private final UpdatePostUserDomainPort updatePostPort;

    @Transactional
    @Override
    public void execute(DeleteUserByIdCommand query) {

        // user soft delete
        User user = loadUserPort.loadUserById(FindByUserIdQuery.of(query.getUserId()));
        user.updateIsDeleted(true);
        user.updateDeletedAt(LocalDateTime.now());
        updateUserPort.updateUser(user);

        // user가 쓴 게시글의 작성자를 익명 사용자로 변경
        List<Post> postList = loadPostPort.loadPostListByUser(FindByUserIdQuery.of(user.getId()));
        if (!postList.isEmpty()) {
            // 익명 사용자 조회
            User systemUser = loadUserPort.loadUserByRole(FindUserByRoleQuery.of(UserRole.SYSTEM));
            Long systemUserId = systemUser.getId();
            // post update
            postList.forEach(p -> p.updateUserId(systemUserId));
            updatePostPort.updatePostListBySystemUser(postList);
        }
    }
}
