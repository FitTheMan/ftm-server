package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import java.util.Set;
import lombok.Getter;

@Getter
public class GroomingTestsInfoResponse {

    private final Integer totalCount;
    private final Set<GroomingTestQuestionWithAnswersVo> groomingTests;

    GroomingTestsInfoResponse(Set<GroomingTestQuestionWithAnswersVo> groomingTests) {
        this.totalCount = groomingTests.size();
        this.groomingTests = groomingTests;
    }

    public static GroomingTestsInfoResponse from(
            Set<GroomingTestQuestionWithAnswersVo> groomingTests) {
        return new GroomingTestsInfoResponse(groomingTests);
    }
}
