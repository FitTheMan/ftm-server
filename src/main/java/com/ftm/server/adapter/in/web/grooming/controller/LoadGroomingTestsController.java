package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.response.GroomingTestsInfoResponse;
import com.ftm.server.application.port.in.grooming.LoadGroomingTestsUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadGroomingTestsController {

    private final LoadGroomingTestsUseCase loadGroomingTestsUseCase;

    @GetMapping("/api/grooming/tests")
    public ResponseEntity<ApiResponse<GroomingTestsInfoResponse>> loadGroomingTests() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                GroomingTestsInfoResponse.from(
                                        loadGroomingTestsUseCase.execute())));
    }
}
