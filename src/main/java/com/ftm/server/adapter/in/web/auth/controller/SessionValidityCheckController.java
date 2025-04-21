package com.ftm.server.adapter.in.web.auth.controller;

import com.ftm.server.adapter.in.web.auth.dto.response.SessionValidityCheckResponse;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionValidityCheckController {

    @GetMapping("/api/auth/session/validity")
    public ResponseEntity<ApiResponse<SessionValidityCheckResponse>> sessionValidationCheck(
            HttpServletRequest req, HttpServletResponse res) {

        Boolean isValid = true;

        HttpSession session = req.getSession(false); // session 조회

        if (session == null) {
            isValid = false;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK, SessionValidityCheckResponse.of(isValid)));
    }
}
