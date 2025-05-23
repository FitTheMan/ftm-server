package com.ftm.server.application.command.grooming.answer;

import lombok.Getter;

@Getter
public class UpdateGroomingTestAnswerCommand {

    private final Long id;
    private final Long questionId;
    private final String answer;
    private final Integer score;

    private UpdateGroomingTestAnswerCommand(
            Long id, Long questionId, String answer, Integer score) {
        this.id = id;
        this.questionId = questionId;
        this.answer = answer;
        this.score = score;
    }

    public static UpdateGroomingTestAnswerCommand of(
            Long id, Long questionId, String answer, Integer score) {
        return new UpdateGroomingTestAnswerCommand(id, questionId, answer, score);
    }
}
