package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class DeleteUserByIdCommand {
    private final Long userId;

    public static DeleteUserByIdCommand of(Long userId) {
        return new DeleteUserByIdCommand(userId);
    }
}
