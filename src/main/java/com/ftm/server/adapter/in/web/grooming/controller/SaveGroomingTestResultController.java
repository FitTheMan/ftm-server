package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestResultRequest;
import com.ftm.server.application.command.grooming.SaveGroomingTestResultCommand;
import com.ftm.server.application.port.in.grooming.SaveGroomingTestResultUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SaveGroomingTestResultController {

    private final SaveGroomingTestResultUseCase saveGroomingTestResultUseCase;

    @PostMapping("/api/grooming/tests")
    public ResponseEntity<ApiResponse<Void>> saveGroomingTestResult(
            @RequestBody SaveGroomingTestResultRequest request) {
        saveGroomingTestResultUseCase.execute(SaveGroomingTestResultCommand.from(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
