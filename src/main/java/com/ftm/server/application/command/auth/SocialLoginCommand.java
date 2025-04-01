package com.ftm.server.application.command.auth;

import lombok.Getter;

@Getter
public abstract class SocialLoginCommand {

    private final String authorizationCode;

    protected SocialLoginCommand(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
