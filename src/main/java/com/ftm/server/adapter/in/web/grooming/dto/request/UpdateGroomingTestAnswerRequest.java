package com.ftm.server.adapter.in.web.grooming.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroomingTestAnswerRequest {

    private Long questionId;
    private String answer;
    private Integer score;
}
