package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.DeleteUserByIdCommand;
import com.ftm.server.application.port.in.user.UserExitUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadUserPort;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.domain.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserExitService implements UserExitUseCase {

    private final LoadUserPort loadUserPort;

    private final UpdateUserPort updateUserPort;

    @Transactional
    @Override
    public void execute(DeleteUserByIdCommand query) {

        // user soft delete
        User user = loadUserPort.loadUserById(FindByUserIdQuery.of(query.getUserId()));
        user.updateIsDeleted(true);
        user.updateDeletedAt(LocalDateTime.now());
        updateUserPort.updateUser(user);
    }
}
