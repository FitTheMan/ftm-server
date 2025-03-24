package com.ftm.server.domain.dto.command;

import com.ftm.server.adapter.dto.request.EmailAuthenticationRequest;
import lombok.Data;

@Data
public class EmailAuthenticationCommand {
    private final String email;

    public static EmailAuthenticationCommand from(EmailAuthenticationRequest request) {
        return new EmailAuthenticationCommand(request.getEmail());
    }
}
