package com.ftm.server.domain.dto.command;

import com.ftm.server.adapter.dto.request.UserLoginRequest;
import lombok.Getter;

@Getter
public class UserLoginCommand {

    private final String email;
    private final String password;

    private UserLoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserLoginCommand from(UserLoginRequest request) {
        return new UserLoginCommand(request.getEmail(), request.getPassword());
    }
}
