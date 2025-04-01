package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.EmailAuthenticationCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface EmailAuthenticationUseCase {

    void execute(EmailAuthenticationCommand command);
}
