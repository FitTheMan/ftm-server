package com.ftm.server.adapter.in.web.grooming.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroomingTestSubmissionRequest {

    private List<SubmittedQuestion> submissions;

    @Getter
    @AllArgsConstructor
    public static class SubmittedQuestion {

        private Long questionId;
        private String groomingCategory;
        private List<Long> answerIds;
    }
}
