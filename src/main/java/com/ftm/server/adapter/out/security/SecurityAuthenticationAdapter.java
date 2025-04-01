package com.ftm.server.adapter.out.security;

import com.ftm.server.application.command.auth.GeneralLoginCommand;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.domain.entity.User;
import com.ftm.server.infrastructure.security.UserPrincipal;
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
@Adapter
@RequiredArgsConstructor
public class SecurityAuthenticationAdapter implements SecurityAuthenticationPort {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final PasswordEncoder passwordEncoder;

    // end::[]]

    @Override
    public Authentication createAuthenticationFromCredentials(GeneralLoginCommand command)
            throws AuthenticationException {
        // 인증 토큰 생성 (입력받은 이메일과 비밀번호로 인증 시도용 토큰 생성)
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword());

        // 인증 수행 (UserPrincipalAdapter 호출, PasswordEncoder 비밀번호 검증 포함)
        return authenticationManager.authenticate(token);
    }

    @Override
    public Authentication createAuthenticationFromSocial(User user) {
        UserPrincipal userPrincipal = UserPrincipal.of(user);
        return new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
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
