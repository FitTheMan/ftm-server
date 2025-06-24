package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.DeleteUserByEmailCommand;
import com.ftm.server.common.annotation.Port;
import org.springframework.security.core.parameters.P;

@Port
public interface UserHardDeleteByEmailUseCase {
    void execute(DeleteUserByEmailCommand command);
}
