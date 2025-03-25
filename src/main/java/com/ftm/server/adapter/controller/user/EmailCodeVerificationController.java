package com.ftm.server.adapter.controller.user;

import com.ftm.server.adapter.dto.request.EmailCodeVerificationRequest;
import com.ftm.server.adapter.dto.response.EmailCodeVerificationResponse;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.domain.usecase.user.EmailCodeVerificationUseCase;
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
