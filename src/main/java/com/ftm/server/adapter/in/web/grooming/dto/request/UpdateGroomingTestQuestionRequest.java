package com.ftm.server.adapter.in.web.grooming.dto.request;

import com.ftm.server.domain.enums.GroomingCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroomingTestQuestionRequest {

    private GroomingCategory groomingCategory;
    private String question;
}
