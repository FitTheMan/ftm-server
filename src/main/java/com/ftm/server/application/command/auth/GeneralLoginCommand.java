package com.ftm.server.application.command.auth;

import com.ftm.server.adapter.in.web.auth.dto.request.GeneralLoginRequest;
import lombok.Getter;

@Getter
public class GeneralLoginCommand {

    private final String email;
    private final String password;

    private GeneralLoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static GeneralLoginCommand from(GeneralLoginRequest request) {
        return new GeneralLoginCommand(request.getEmail(), request.getPassword());
    }
}
