package com.ftm.server.application.command.grooming;

import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestResultRequest;
import java.util.List;
import lombok.Getter;

@Getter
public class SaveGroomingTestResultCommand {

    private final Long userId;
    private final Long groomingLevelId;
    private final Integer totalScore;
    private final List<GroomingTestResult> results;

    @Getter
    public static class GroomingTestResult {
        private final Long questionId;
        private final List<Long> answerIds;

        private GroomingTestResult(Long questionId, List<Long> answerIds) {
            this.questionId = questionId;
            this.answerIds = answerIds;
        }

        public static GroomingTestResult of(Long questionId, List<Long> answerIds) {
            return new GroomingTestResult(questionId, answerIds);
        }
    }

    private SaveGroomingTestResultCommand(SaveGroomingTestResultRequest request) {
        this.userId = request.getUserId();
        this.groomingLevelId = request.getGroomingLevelId();
        this.totalScore = request.getTotalScore();
        this.results =
                request.getResults().stream()
                        .map(
                                result ->
                                        GroomingTestResult.of(
                                                result.getQuestionId(), result.getAnswerIds()))
                        .toList();
    }

    public static SaveGroomingTestResultCommand from(SaveGroomingTestResultRequest request) {
        return new SaveGroomingTestResultCommand(request);
    }
}
