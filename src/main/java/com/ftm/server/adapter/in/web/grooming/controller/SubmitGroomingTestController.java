package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.request.GroomingTestSubmissionRequest;
import com.ftm.server.adapter.in.web.grooming.dto.response.GroomingTestResultResponse;
import com.ftm.server.adapter.in.web.grooming.mapper.GroomingTestCommandMapper;
import com.ftm.server.application.port.in.grooming.SubmitGroomingTestUseCase;
import com.ftm.server.application.vo.grooming.GroomingTestResultVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubmitGroomingTestController {

    private final SubmitGroomingTestUseCase submitGroomingTestUseCase;

    @PostMapping("/api/grooming/tests/submission")
    public ResponseEntity<ApiResponse<GroomingTestResultResponse>> completeGroomingTests(
            @RequestBody GroomingTestSubmissionRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        GroomingTestResultVo results =
                submitGroomingTestUseCase.execute(
                        GroomingTestCommandMapper.toSubmitGroomingTestCommand(
                                userPrincipal, request));

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK, GroomingTestResultResponse.from(results)));
    }
}
