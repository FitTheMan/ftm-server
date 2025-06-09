package com.ftm.server.adapter.in.web.grooming.controller.level;

import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingLevelRequest;
import com.ftm.server.application.command.grooming.level.UpdateGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.UpdateGroomingLevelUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateGroomingLevelController {

    private final UpdateGroomingLevelUseCase updateGroomingLevelUseCase;

    @PatchMapping("/api/grooming/levels/{levelId}")
    public ResponseEntity<ApiResponse<Void>> saveGroomingLevel(
            @PathVariable Long levelId, @RequestBody @Valid UpdateGroomingLevelRequest request) {

        UpdateGroomingLevelCommand command =
                UpdateGroomingLevelCommand.of(
                        levelId,
                        request.getMinScore(),
                        request.getMaxScore(),
                        request.getNormalModeName(),
                        request.getNormalModeSummary(),
                        request.getNormalModeDescription(),
                        request.getTruthModeName(),
                        request.getTruthModeSummary(),
                        request.getTruthModeDescription());
        updateGroomingLevelUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
