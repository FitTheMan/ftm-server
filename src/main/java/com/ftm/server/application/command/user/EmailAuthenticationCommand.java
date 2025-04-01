package com.ftm.server.application.command.user;

import com.ftm.server.adapter.in.web.user.dto.request.EmailAuthenticationRequest;
import lombok.Data;

@Data
public class EmailAuthenticationCommand {
    private final String email;

    public static EmailAuthenticationCommand from(EmailAuthenticationRequest request) {
        return new EmailAuthenticationCommand(request.getEmail());
    }
}
