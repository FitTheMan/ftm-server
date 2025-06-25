package com.ftm.server.application.command.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecoverUserAccountCommand {
    private String email;

    public static RecoverUserAccountCommand of(String email) {
        return new RecoverUserAccountCommand(email);
    }
}
