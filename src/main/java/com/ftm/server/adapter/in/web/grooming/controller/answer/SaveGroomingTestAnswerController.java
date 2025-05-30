package com.ftm.server.adapter.in.web.grooming.controller.answer;

import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestAnswerRequest;
import com.ftm.server.application.command.grooming.answer.SaveGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.SaveGroomingTestAnswerUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SaveGroomingTestAnswerController {

    private final SaveGroomingTestAnswerUseCase saveGroomingTestAnswerUseCase;

    @PostMapping("/api/grooming/tests/questions/{questionsId}/answers")
    public ResponseEntity<ApiResponse<Void>> saveAnswer(
            @PathVariable Long questionsId,
            @RequestBody @Valid SaveGroomingTestAnswerRequest request) {
        SaveGroomingTestAnswerCommand command =
                SaveGroomingTestAnswerCommand.of(
                        questionsId, request.getAnswer(), request.getScore());
        saveGroomingTestAnswerUseCase.execute(
                SaveGroomingTestAnswerCommand.of(
                        questionsId, request.getAnswer(), request.getScore()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
