package com.ftm.server.application.port.in.auth;

import com.ftm.server.common.annotation.UseCase;
import jakarta.servlet.http.Cookie;

@UseCase
public interface LogoutUseCase {

    Cookie execute();
}
