package com.ftm.server.adapter.in.web.grooming.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveGroomingTestAnswerRequest {

    @NotEmpty private String answer;

    @Min(0)
    private Integer score;
}
