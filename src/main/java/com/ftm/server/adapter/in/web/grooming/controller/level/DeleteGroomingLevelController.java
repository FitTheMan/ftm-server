package com.ftm.server.adapter.in.web.grooming.controller.level;

import com.ftm.server.application.command.grooming.level.DeleteGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.DeleteGroomingLevelUseCase;
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
public class DeleteGroomingLevelController {

    private final DeleteGroomingLevelUseCase deleteGroomingLevelUseCase;

    @DeleteMapping("/api/grooming/levels/{levelId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroomingLevel(@PathVariable Long levelId) {
        DeleteGroomingLevelCommand command = DeleteGroomingLevelCommand.of(levelId);
        deleteGroomingLevelUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
