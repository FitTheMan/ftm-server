package com.ftm.server.application.dto.query;

import lombok.Data;

@Data
public class FindEmailVerificationLogsByEmailQuery {
    private final String email;

    public static FindEmailVerificationLogsByEmailQuery of(String email) {
        return new FindEmailVerificationLogsByEmailQuery(email);
    }
}
