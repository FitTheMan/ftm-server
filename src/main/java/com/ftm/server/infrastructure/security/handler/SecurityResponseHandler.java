package com.ftm.server.infrastructure.security.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftm.server.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 시큐리티 필터단에서 응답을 처리하는 핸들러 */
@Component
@RequiredArgsConstructor
public class SecurityResponseHandler {

    private final ObjectMapper objectMapper;

    public <T> void sendResponse(HttpServletResponse response, ApiResponse<T> apiResponse)
            throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setStatus(apiResponse.getStatus());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
