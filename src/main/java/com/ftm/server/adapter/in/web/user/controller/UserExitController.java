package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.application.command.user.DeleteUserByIdCommand;
import com.ftm.server.application.port.in.user.UserExitUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserExitController {

    private final UserExitUseCase userExitUseCase;

    @DeleteMapping("api/users")
    public ResponseEntity<ApiResponse> userExit(
            @AuthenticationPrincipal UserPrincipal user, HttpServletRequest request) {
        // 회원 탈퇴
        userExitUseCase.execute(DeleteUserByIdCommand.of(user.getId()));
        // 로그아웃 처리
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
