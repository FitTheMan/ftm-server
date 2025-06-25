package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.request.RecoverUserAccountRequest;
import com.ftm.server.adapter.in.web.user.dto.response.RecoverUserAccountResponse;
import com.ftm.server.application.command.user.RecoverUserAccountCommand;
import com.ftm.server.application.port.in.user.RecoverUserAccountUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecoverUserAccountController {

    private final RecoverUserAccountUseCase recoverUserAccountUseCase;

    @PostMapping("/api/users/me/recover")
    public ResponseEntity<ApiResponse> recoverUserAccount(
            @Valid @RequestBody RecoverUserAccountRequest request) {
        RecoverUserAccountResponse response =
                RecoverUserAccountResponse.from(
                        recoverUserAccountUseCase.execute(
                                RecoverUserAccountCommand.of(request.getEmail())));
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, response));
    }
}
