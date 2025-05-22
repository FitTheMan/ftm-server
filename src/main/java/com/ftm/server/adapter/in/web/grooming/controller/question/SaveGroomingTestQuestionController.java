package com.ftm.server.adapter.in.web.grooming.controller.question;

import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestQuestionRequest;
import com.ftm.server.application.command.grooming.SaveGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.SaveGroomingTestQuestionUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SaveGroomingTestQuestionController {

    private final SaveGroomingTestQuestionUseCase saveGroomingTestQuestionUseCase;

    @PostMapping("/api/grooming/tests/questions")
    public ResponseEntity<ApiResponse<Void>> saveQuestion(
            @RequestBody @Valid SaveGroomingTestQuestionRequest request) {
        saveGroomingTestQuestionUseCase.execute(
                SaveGroomingTestQuestionCommand.of(
                        request.getGroomingCategory(), request.getQuestion()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
