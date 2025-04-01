package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.request.EmailCodeVerificationRequest;
import com.ftm.server.adapter.in.web.user.dto.response.EmailCodeVerificationResponse;
import com.ftm.server.application.port.in.user.EmailCodeVerificationUseCase;
import com.ftm.server.application.query.EmailCodeVerificationQuery;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailCodeVerificationController {

    private final EmailCodeVerificationUseCase emailCodeVerificationUseCase;

    @PostMapping("/api/users/email/authentication/code")
    public ResponseEntity<ApiResponse<EmailCodeVerificationResponse>> controller(
            @Valid @RequestBody EmailCodeVerificationRequest request) {

        EmailCodeVerificationResponse response =
                EmailCodeVerificationResponse.from(
                        emailCodeVerificationUseCase.execute(
                                EmailCodeVerificationQuery.from(request)));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, response));
    }
}
