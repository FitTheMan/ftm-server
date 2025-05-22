package com.ftm.server.adapter.in.web.grooming.controller.question;

import com.ftm.server.application.command.grooming.DeleteGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.DeleteGroomingTestQuestionUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteGroomingTestQuestionController {

    private final DeleteGroomingTestQuestionUseCase deleteGroomingTestQuestionUseCase;

    @DeleteMapping("/api/grooming/tests/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        deleteGroomingTestQuestionUseCase.execute(DeleteGroomingTestQuestionCommand.of(questionId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
