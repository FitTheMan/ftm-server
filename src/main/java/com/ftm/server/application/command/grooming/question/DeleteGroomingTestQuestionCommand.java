package com.ftm.server.application.command.grooming.question;

import lombok.Getter;

@Getter
public class DeleteGroomingTestQuestionCommand {

    private final Long id;

    private DeleteGroomingTestQuestionCommand(Long id) {
        this.id = id;
    }

    public static DeleteGroomingTestQuestionCommand of(Long id) {
        return new DeleteGroomingTestQuestionCommand(id);
    }
}
