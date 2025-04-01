package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.response.EmailDuplicationCheckResponse;
import com.ftm.server.application.port.in.user.EmailDuplicationCheckUseCase;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailDuplicationCheckController {

    private final EmailDuplicationCheckUseCase emailDuplicationCheckUseCase;

    @GetMapping("api/users/email/duplication")
    public ResponseEntity<ApiResponse<EmailDuplicationCheckResponse>> emailDuplicationCheck(
            @RequestParam(value = "email") String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                EmailDuplicationCheckResponse.from(
                                        emailDuplicationCheckUseCase.execute(
                                                FindByEmailQuery.of(email)))));
    }
}
