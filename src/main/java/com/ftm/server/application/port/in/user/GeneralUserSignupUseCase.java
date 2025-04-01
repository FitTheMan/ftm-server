package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.GeneralUserSignupCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface GeneralUserSignupUseCase {
    void execute(GeneralUserSignupCommand command);
}
