package com.ftm.server.adapter.in.web.grooming.controller.level;

import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingLevelRequest;
import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.SaveGroomingLevelUseCase;
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
public class SaveGroomingLevelController {

    private final SaveGroomingLevelUseCase saveGroomingLevelUseCase;

    @PostMapping("/api/grooming/levels")
    public ResponseEntity<ApiResponse<Void>> saveGroomingLevel(
            @RequestBody @Valid SaveGroomingLevelRequest request) {
        SaveGroomingLevelCommand command =
                SaveGroomingLevelCommand.of(
                        request.getMinScore(),
                        request.getMaxScore(),
                        request.getNormalModeName(),
                        request.getNormalModeSummary(),
                        request.getNormalModeDescription(),
                        request.getTruthModeName(),
                        request.getTruthModeSummary(),
                        request.getTruthModeDescription(),
                        request.getImagePath());
        saveGroomingLevelUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
