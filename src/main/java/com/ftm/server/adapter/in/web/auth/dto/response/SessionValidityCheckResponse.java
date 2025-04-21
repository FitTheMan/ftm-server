package com.ftm.server.adapter.in.web.auth.dto.response;

import lombok.Data;

@Data
public class SessionValidityCheckResponse {
    private final Boolean isValid;

    public static SessionValidityCheckResponse of(Boolean isValid) {
        return new SessionValidityCheckResponse(isValid);
    }
}
