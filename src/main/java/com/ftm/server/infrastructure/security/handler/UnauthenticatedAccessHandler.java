package com.ftm.server.infrastructure.security.handler;

import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** 인증이 필요한 요청에서 인증되지 않은 유저가 요청할 경우 예외처리하는 핸들러 */
@Component
@RequiredArgsConstructor
public class UnauthenticatedAccessHandler implements AuthenticationEntryPoint {

    private final SecurityResponseHandler securityResponseHandler;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        securityResponseHandler.sendResponse(
                response, ApiResponse.fail(ErrorResponseCode.NOT_AUTHENTICATED));
    }
}
