package com.ftm.server.adapter.controller.auth;

import com.ftm.server.adapter.dto.request.UserLoginRequest;
import com.ftm.server.adapter.dto.response.UserLoginResponse;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.dto.command.UserLoginCommand;
import com.ftm.server.domain.usecase.auth.UserLoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginUseCase loginUseCase;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(
            @RequestBody UserLoginRequest request,
            HttpServletRequest req,
            HttpServletResponse res) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                UserLoginResponse.from(
                                        loginUseCase.login(
                                                UserLoginCommand.from(request), req, res))));
    }
}
