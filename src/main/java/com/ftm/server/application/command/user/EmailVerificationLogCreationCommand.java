package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class EmailVerificationLogCreationCommand {
    private final String email;
    private final String verificationCode;

    public static EmailVerificationLogCreationCommand of(String email, String verificationCode) {
        return new EmailVerificationLogCreationCommand(email, verificationCode);
    }
}
