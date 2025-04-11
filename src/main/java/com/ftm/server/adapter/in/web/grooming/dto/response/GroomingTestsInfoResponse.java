package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.ftm.server.application.vo.grooming.GroomingTestAnswerInfoVo;
import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.domain.enums.GroomingCategory;
import java.util.List;
import lombok.Getter;

@Getter
public class GroomingTestsInfoResponse {

    private final Integer totalCount;
    private final List<GroomingTestQuestionWithAnswersResponse> groomingTests;

    GroomingTestsInfoResponse(List<GroomingTestQuestionWithAnswersVo> groomingTests) {
        this.totalCount = groomingTests.size();
        this.groomingTests =
                groomingTests.stream().map(GroomingTestQuestionWithAnswersResponse::from).toList();
    }

    public static GroomingTestsInfoResponse from(
            List<GroomingTestQuestionWithAnswersVo> groomingTests) {
        return new GroomingTestsInfoResponse(groomingTests);
    }

    @Getter
    public static class GroomingTestQuestionWithAnswersResponse {
        private final Long groomingTestQuestionId;
        private final String question;
        private final GroomingCategory groomingCategory;
        private final List<GroomingTestAnswerInfoResponse> answers;

        GroomingTestQuestionWithAnswersResponse(
                GroomingTestQuestionWithAnswersVo groomingTestQuestionWithAnswersVo) {
            this.groomingTestQuestionId =
                    groomingTestQuestionWithAnswersVo.getGroomingTestQuestionId();
            this.question = groomingTestQuestionWithAnswersVo.getQuestion();
            this.groomingCategory = groomingTestQuestionWithAnswersVo.getGroomingCategory();
            this.answers =
                    groomingTestQuestionWithAnswersVo.getAnswers().stream()
                            .map(GroomingTestAnswerInfoResponse::from)
                            .toList();
        }

        public static GroomingTestQuestionWithAnswersResponse from(
                GroomingTestQuestionWithAnswersVo groomingTestQuestionWithAnswersVo) {
            return new GroomingTestQuestionWithAnswersResponse(groomingTestQuestionWithAnswersVo);
        }

        @Getter
        public static class GroomingTestAnswerInfoResponse {
            private final Long groomingTestAnswerId;
            private final String answer;

            GroomingTestAnswerInfoResponse(GroomingTestAnswerInfoVo groomingTestAnswerInfoVo) {
                this.groomingTestAnswerId = groomingTestAnswerInfoVo.getGroomingTestAnswerId();
                this.answer = groomingTestAnswerInfoVo.getAnswer();
            }

            public static GroomingTestAnswerInfoResponse from(
                    GroomingTestAnswerInfoVo groomingTestAnswerInfoVo) {
                return new GroomingTestAnswerInfoResponse(groomingTestAnswerInfoVo);
            }
        }
    }
}
