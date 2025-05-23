package com.ftm.server.application.command.grooming.answer;

import lombok.Getter;

@Getter
public class SaveGroomingTestAnswerCommand {

    private final Long questionId;
    private final String answer;
    private final Integer score;

    private SaveGroomingTestAnswerCommand(Long questionId, String answer, Integer score) {
        this.questionId = questionId;
        this.answer = answer;
        this.score = score;
    }

    public static SaveGroomingTestAnswerCommand of(Long questionId, String answer, Integer score) {
        return new SaveGroomingTestAnswerCommand(questionId, answer, score);
    }
}
