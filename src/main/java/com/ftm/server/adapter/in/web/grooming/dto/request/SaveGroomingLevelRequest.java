package com.ftm.server.adapter.in.web.grooming.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveGroomingLevelRequest {

    @Min(0)
    @Max(100)
    private Integer minScore;

    @Min(0)
    @Max(100)
    private Integer maxScore;

    @NotEmpty private String normalModeName;
    @NotEmpty private String normalModeSummary;
    @NotEmpty private String normalModeDescription;
    @NotEmpty private String truthModeName;
    @NotEmpty private String truthModeSummary;
    @NotEmpty private String truthModeDescription;
    private String imagePath;
}
