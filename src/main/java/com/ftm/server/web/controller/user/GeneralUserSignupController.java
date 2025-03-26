package com.ftm.server.web.controller.user;

import com.ftm.server.application.dto.command.GeneralUserSignupCommand;
import com.ftm.server.application.usecase.user.GeneralUserSignupUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.web.dto.request.GeneralUserSignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GeneralUserSignupController {

    private final GeneralUserSignupUseCase generalUserSignupUseCase;

    @PostMapping("/api/users")
    public ResponseEntity<ApiResponse> createGeneralUser(
            @Valid @RequestBody GeneralUserSignupRequest request) {
        generalUserSignupUseCase.execute(GeneralUserSignupCommand.from(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
