package com.ftm.server.adapter.controller.user;

import com.ftm.server.adapter.dto.request.GeneralUserSignupRequest;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.dto.command.GeneralUserSignupCommand;
import com.ftm.server.domain.usecase.user.GeneralUserSignupUseCase;
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
