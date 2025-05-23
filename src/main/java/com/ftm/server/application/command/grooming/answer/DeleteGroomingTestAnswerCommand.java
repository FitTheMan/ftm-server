package com.ftm.server.application.command.grooming.answer;

import lombok.Getter;

@Getter
public class DeleteGroomingTestAnswerCommand {

    private final Long id;

    private DeleteGroomingTestAnswerCommand(Long id) {
        this.id = id;
    }

    public static DeleteGroomingTestAnswerCommand of(Long id) {
        return new DeleteGroomingTestAnswerCommand(id);
    }
}
