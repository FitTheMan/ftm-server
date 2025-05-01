package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.DeleteUserByIdCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface UserExitUseCase {
    void execute(DeleteUserByIdCommand query);
}
