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
        private final List<SelectedAnswer> answers;

        private SubmittedQuestion(
                Long questionId, String groomingCategory, List<SelectedAnswer> answers) {
            this.questionId = questionId;
            this.groomingCategory = groomingCategory;
            this.answers = answers;
        }

        public static SubmittedQuestion of(
                Long questionId, String groomingCategory, List<SelectedAnswer> answers) {
            return new SubmittedQuestion(questionId, groomingCategory, answers);
        }

        @Getter
        public static class SelectedAnswer {

            private final Long answerId;
            private final int score;

            private SelectedAnswer(Long answerId, int score) {
                this.answerId = answerId;
                this.score = score;
            }

            public static SelectedAnswer of(Long answerId, int score) {
                return new SelectedAnswer(answerId, score);
            }
        }
    }
}
