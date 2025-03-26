package com.ftm.server.web.controller.user;

import com.ftm.server.application.usecase.user.GetUserSignupOptionsUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.web.dto.response.UserSignupOptionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserSignupOptionsController {

    private final GetUserSignupOptionsUseCase getUserSignupOptionsUseCase;

    @GetMapping("/api/users/options")
    public ResponseEntity<ApiResponse<UserSignupOptionsResponse>> getUserSignupOptions() {
        UserSignupOptionsResponse response =
                UserSignupOptionsResponse.from(getUserSignupOptionsUseCase.execute());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, response));
    }
}
