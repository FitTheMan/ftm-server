package com.ftm.server.adapter.in.web.auth.controller;

import com.ftm.server.adapter.in.web.auth.dto.request.GeneralLoginRequest;
import com.ftm.server.adapter.in.web.auth.dto.response.GeneralLoginResponse;
import com.ftm.server.application.command.auth.GeneralLoginCommand;
import com.ftm.server.application.port.in.auth.GeneralLoginUseCase;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
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
public class GeneralLoginController {

    private final GeneralLoginUseCase generalLoginUseCase;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<GeneralLoginResponse>> login(
            @RequestBody GeneralLoginRequest request,
            HttpServletRequest req,
            HttpServletResponse res) {
        AuthenticatedUserVo vo =
                generalLoginUseCase.execute(GeneralLoginCommand.from(request), req, res);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, GeneralLoginResponse.from(vo)));
    }
}
