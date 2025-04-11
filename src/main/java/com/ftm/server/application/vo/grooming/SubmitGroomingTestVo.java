package com.ftm.server.application.vo.grooming;

import com.ftm.server.application.command.grooming.SaveGroomingTestResultCommand;
import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import java.util.List;
import lombok.Getter;

@Getter
public class SubmitGroomingTestVo {

    private final Long questionId;
    private final List<Long> answerIds;

    private SubmitGroomingTestVo(Long questionId, List<Long> answerIds) {
        this.questionId = questionId;
        this.answerIds = answerIds;
    }

    public static SubmitGroomingTestVo of(Long questionId, List<Long> answerIds) {
        return new SubmitGroomingTestVo(questionId, answerIds);
    }

    public static List<SubmitGroomingTestVo> from(SubmitGroomingTestCommand command) {
        return command.getSubmissions().stream()
                .map(
                        submission ->
                                SubmitGroomingTestVo.of(
                                        submission.getQuestionId(),
                                        submission.getAnswers().stream()
                                                .map(
                                                        SubmitGroomingTestCommand.SubmittedQuestion
                                                                        .SelectedAnswer
                                                                ::getAnswerId)
                                                .toList()))
                .toList();
    }

    public static List<SubmitGroomingTestVo> from(SaveGroomingTestResultCommand command) {
        return command.getResults().stream()
                .map(
                        result ->
                                SubmitGroomingTestVo.of(
                                        result.getQuestionId(), result.getAnswerIds()))
                .toList();
    }
}
