package com.ftm.server.application.command.grooming;

import java.util.List;
import lombok.Getter;

@Getter
public class SubmitGroomingTestCommand {

    private final Long userId;
    private final List<SubmittedQuestion> submissions;

    protected SubmitGroomingTestCommand(Long userId, List<SubmittedQuestion> submissions) {
        this.userId = userId;
        this.submissions = submissions;
    }

    public static SubmitGroomingTestCommand from(Long userId, List<SubmittedQuestion> submissions) {
        return new SubmitGroomingTestCommand(userId, submissions);
    }

    @Getter
    public static class SubmittedQuestion {

        private final Long questionId;
        private final String groomingCategory;
        private final List<Long> answerIds;

        private SubmittedQuestion(Long questionId, String groomingCategory, List<Long> answerIds) {
            this.questionId = questionId;
            this.groomingCategory = groomingCategory;
            this.answerIds = answerIds;
        }

        public static SubmittedQuestion of(
                Long questionId, String groomingCategory, List<Long> answerIds) {
            return new SubmittedQuestion(questionId, groomingCategory, answerIds);
        }
    }
}
