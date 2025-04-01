package com.ftm.server.application.port.in.auth;

import com.ftm.server.application.command.auth.GeneralLoginCommand;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import com.ftm.server.common.annotation.UseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@UseCase
public interface GeneralLoginUseCase {

    AuthenticatedUserVo execute(
            GeneralLoginCommand command, HttpServletRequest req, HttpServletResponse res);
}
