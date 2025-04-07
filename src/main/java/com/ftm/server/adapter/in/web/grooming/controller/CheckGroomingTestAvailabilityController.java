package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.response.CheckGroomingTestAvailabilityResponse;
import com.ftm.server.application.port.in.grooming.CheckGroomingTestAvailabilityUseCase;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.grooming.GroomingTestAvailabilityVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckGroomingTestAvailabilityController {

    private final CheckGroomingTestAvailabilityUseCase checkGroomingTestAvailabilityUseCase;

    @GetMapping("/api/grooming/tests/availability")
    public ResponseEntity<ApiResponse<CheckGroomingTestAvailabilityResponse>> checkAvailability(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        GroomingTestAvailabilityVo result =
                checkGroomingTestAvailabilityUseCase.execute(
                        FindByUserIdQuery.of(userPrincipal.getId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                CheckGroomingTestAvailabilityResponse.from(result)));
    }
}
