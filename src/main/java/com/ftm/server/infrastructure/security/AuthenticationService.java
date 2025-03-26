package com.ftm.server.infrastructure.security;

import com.ftm.server.adapter.gateway.AuthenticationGateway;
import com.ftm.server.common.annotation.InfraService;
import com.ftm.server.domain.dto.command.UserLoginCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

@Slf4j
@InfraService
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationGateway {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final PasswordEncoder passwordEncoder;

    // end::[]]

    @Override
    public Authentication createAuthenticationFromCredentials(UserLoginCommand command)
            throws AuthenticationException {
        // 인증 토큰 생성 (입력받은 이메일과 비밀번호로 인증 시도용 토큰 생성)
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword());

        // 인증 수행 (UserPrincipalService 호출, PasswordEncoder 비밀번호 검증 포함)
        return authenticationManager.authenticate(token);
    }

    @Override
    public void saveAuthenticatedSession(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 시큐리티 컨텍스트 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // 생성한 시큐리티 컨텍스트를 Redis 세션에 저장
        securityContextRepository.saveContext(context, request, response);
    }

    @Override
    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }
}
