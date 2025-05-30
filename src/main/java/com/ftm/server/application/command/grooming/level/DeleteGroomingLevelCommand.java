package com.ftm.server.application.command.grooming.level;

import lombok.Getter;

@Getter
public class DeleteGroomingLevelCommand {

    private final Long id;

    private DeleteGroomingLevelCommand(Long id) {
        this.id = id;
    }

    public static DeleteGroomingLevelCommand of(Long id) {
        return new DeleteGroomingLevelCommand(id);
    }
}
