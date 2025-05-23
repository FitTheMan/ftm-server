package com.ftm.server.adapter.in.web.grooming.controller.answer;

import com.ftm.server.application.command.grooming.answer.DeleteGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.DeleteGroomingTestAnswerUseCase;
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
public class DeleteGroomingTestAnswerController {

    private final DeleteGroomingTestAnswerUseCase deleteGroomingTestAnswerUseCase;

    @DeleteMapping("/api/grooming/tests/answers/{answerId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(@PathVariable Long answerId) {
        deleteGroomingTestAnswerUseCase.execute(DeleteGroomingTestAnswerCommand.of(answerId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
