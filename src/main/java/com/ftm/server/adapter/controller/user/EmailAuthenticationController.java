package com.ftm.server.adapter.controller.user;

import com.ftm.server.adapter.dto.request.EmailAuthenticationRequest;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.dto.command.EmailAuthenticationCommand;
import com.ftm.server.domain.usecase.user.EmailAuthenticationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailAuthenticationController {

    private final EmailAuthenticationUseCase emailAuthenticationUseCase;

    @PostMapping("/api/users/email/authentication")
    public ResponseEntity<ApiResponse<Object>> emailAuthenticationCodeSender(
            @Valid @RequestBody EmailAuthenticationRequest request) {
        emailAuthenticationUseCase.sendEmailAuthenticationCode(
                EmailAuthenticationCommand.from(request)); // command 객체 전달
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
