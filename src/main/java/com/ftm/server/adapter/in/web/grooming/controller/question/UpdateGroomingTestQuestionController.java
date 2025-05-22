package com.ftm.server.adapter.in.web.grooming.controller.question;

import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingTestQuestionRequest;
import com.ftm.server.application.command.grooming.UpdateGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.UpdateGroomingTestQuestionUseCase;
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
public class UpdateGroomingTestQuestionController {

    private final UpdateGroomingTestQuestionUseCase updateGroomingTestQuestionUseCase;

    @PatchMapping("/api/grooming/tests/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> updateQuestion(
            @PathVariable Long questionId, @RequestBody UpdateGroomingTestQuestionRequest request) {
        updateGroomingTestQuestionUseCase.execute(
                UpdateGroomingTestQuestionCommand.of(
                        questionId, request.getGroomingCategory(), request.getQuestion()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
