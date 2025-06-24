package com.ftm.server.application.command.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteUserByEmailCommand {
    private final String email;
    public static DeleteUserByEmailCommand of(String email){
        return new DeleteUserByEmailCommand(email);
    }
}
