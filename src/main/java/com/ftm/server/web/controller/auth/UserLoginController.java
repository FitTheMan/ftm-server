package com.ftm.server.web.controller.auth;

import com.ftm.server.application.dto.command.UserLoginCommand;
import com.ftm.server.application.usecase.auth.UserLoginUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.vo.UserSummaryVo;
import com.ftm.server.web.dto.request.UserLoginRequest;
import com.ftm.server.web.dto.response.UserLoginResponse;
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
        UserSummaryVo vo = loginUseCase.login(UserLoginCommand.from(request), req, res);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, UserLoginResponse.from(vo)));
    }
}
