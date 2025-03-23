package com.ftm.server.infrastructure.security.handler;

import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.stereotype.Component;

/** 인증은 되었지만 접근 권한이 없는 경우 예외처리하는 핸들러 */
@Component
@RequiredArgsConstructor
public class PermissionDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponseHandler securityResponseHandler;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        securityResponseHandler.sendResponse(
                response, ApiResponse.fail(ErrorResponseCode.NOT_AUTHORIZATION));

        SecurityContextPersistenceFilter filter = new SecurityContextPersistenceFilter();
    }
}
