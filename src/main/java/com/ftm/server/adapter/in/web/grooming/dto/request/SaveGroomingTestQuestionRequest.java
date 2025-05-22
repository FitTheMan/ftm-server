package com.ftm.server.adapter.in.web.grooming.dto.request;

import com.ftm.server.domain.enums.GroomingCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveGroomingTestQuestionRequest {

    @NotNull private GroomingCategory groomingCategory;
    @NotEmpty private String question;
}
