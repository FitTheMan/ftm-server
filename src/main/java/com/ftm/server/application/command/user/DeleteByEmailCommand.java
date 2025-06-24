package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class DeleteByEmailCommand {
    private final String email;

    public static DeleteByEmailCommand of(String email) {
        return new DeleteByEmailCommand(email);
    }
} 