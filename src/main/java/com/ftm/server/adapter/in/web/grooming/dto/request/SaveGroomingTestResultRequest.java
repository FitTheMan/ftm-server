package com.ftm.server.adapter.in.web.grooming.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveGroomingTestResultRequest {

    private Long userId;
    private Long groomingLevelId;
    private Integer totalScore;
    private List<GroomingTestResult> results;

    @Getter
    @AllArgsConstructor
    public static class GroomingTestResult {

        private Long questionId;
        private List<Long> answerIds;
    }
}
