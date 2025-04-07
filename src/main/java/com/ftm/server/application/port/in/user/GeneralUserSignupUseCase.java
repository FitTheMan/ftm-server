package com.ftm.server.application.port.in.user;

import com.ftm.server.adapter.in.web.user.dto.response.GeneralUserSignupResponse;
import com.ftm.server.application.command.user.GeneralUserSignupCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface GeneralUserSignupUseCase {
    GeneralUserSignupResponse execute(GeneralUserSignupCommand command);
}
