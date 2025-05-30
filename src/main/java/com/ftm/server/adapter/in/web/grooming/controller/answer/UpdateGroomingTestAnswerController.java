package com.ftm.server.adapter.in.web.grooming.controller.answer;

import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingTestAnswerRequest;
import com.ftm.server.application.command.grooming.answer.UpdateGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.UpdateGroomingTestAnswerUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateGroomingTestAnswerController {

    private final UpdateGroomingTestAnswerUseCase updateGroomingTestAnswerUseCase;

    @PatchMapping("/api/grooming/tests/answers/{answerId}")
    public ResponseEntity<ApiResponse<Void>> updateAnswer(
            @PathVariable Long answerId, @RequestBody UpdateGroomingTestAnswerRequest request) {
        UpdateGroomingTestAnswerCommand command =
                UpdateGroomingTestAnswerCommand.of(
                        answerId, request.getQuestionId(), request.getAnswer(), request.getScore());
        updateGroomingTestAnswerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
