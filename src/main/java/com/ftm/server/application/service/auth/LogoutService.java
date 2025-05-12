package com.ftm.server.application.service.auth;

import static com.ftm.server.common.consts.StaticConsts.CLIENT_SESSION_COOKIE_NAME;

import com.ftm.server.application.port.in.auth.LogoutUseCase;
import com.ftm.server.common.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    @Override
    public Cookie execute() {
        // 클라이언트 세션 쿠키 초기화
        return CookieUtils.invalidateCookie(CLIENT_SESSION_COOKIE_NAME);
    }
}
