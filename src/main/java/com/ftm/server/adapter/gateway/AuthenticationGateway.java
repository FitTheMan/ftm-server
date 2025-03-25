package com.ftm.server.adapter.gateway;

import com.ftm.server.domain.dto.command.UserLoginCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

/** 시큐리티 인증 관련 작업 Gateway */
public interface AuthenticationGateway {

    // 일반 유저 인증 객체 생성
    Authentication createAuthenticationFromCredentials(UserLoginCommand command);

    // 인증 세션 등록 (시큐리티 컨텍스트 저장)
    void saveAuthenticatedSession(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response);
}
