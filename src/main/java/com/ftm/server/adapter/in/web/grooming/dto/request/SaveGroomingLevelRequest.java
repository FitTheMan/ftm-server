package com.ftm.server.adapter.in.web.grooming.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty private String mildLevelName;
    @NotEmpty private String spicyLevelName;
}
