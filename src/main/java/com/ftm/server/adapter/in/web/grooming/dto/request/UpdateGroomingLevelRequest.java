package com.ftm.server.adapter.in.web.grooming.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroomingLevelRequest {

    @Min(0)
    @Max(100)
    private Integer minScore;

    @Min(0)
    @Max(100)
    private Integer maxScore;

    private String normalModeName;
    private String normalModeSummary;
    private String normalModeDescription;
    private String truthModeName;
    private String truthModeSummary;
    private String truthModeDescription;
    private String imagePath;
}
