package com.ftm.server.adapter.controller.user;

import com.ftm.server.adapter.dto.response.EmailDuplicationCheckResponse;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.domain.usecase.user.EmailDuplicationCheckUseCase;
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
                                        emailDuplicationCheckUseCase.emailDuplicationCheck(
                                                FindByEmailQuery.of(email)))));
    }
}
