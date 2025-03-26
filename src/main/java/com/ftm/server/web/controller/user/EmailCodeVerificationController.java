package com.ftm.server.web.controller.user;

import com.ftm.server.application.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.application.usecase.user.EmailCodeVerificationUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.web.dto.request.EmailCodeVerificationRequest;
import com.ftm.server.web.dto.response.EmailCodeVerificationResponse;
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
