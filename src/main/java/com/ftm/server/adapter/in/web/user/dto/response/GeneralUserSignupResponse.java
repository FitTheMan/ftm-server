package com.ftm.server.adapter.in.web.user.dto.response;

import lombok.Data;

@Data
public class GeneralUserSignupResponse {
    private final Long userId;

    public static GeneralUserSignupResponse of(Long userId) {
        return new GeneralUserSignupResponse(userId);
    }
}
