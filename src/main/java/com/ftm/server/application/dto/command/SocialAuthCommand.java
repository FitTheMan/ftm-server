package com.ftm.server.application.dto.command;

import lombok.Getter;

@Getter
public abstract class SocialAuthCommand {

    private final String authorizationCode;

    protected SocialAuthCommand(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
