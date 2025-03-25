package com.ftm.server.domain.dto.vo;

import lombok.Data;

@Data
public class EmailCodeVerificationVo {
    private final Boolean isVerified;

    public static EmailCodeVerificationVo of(Boolean isVerified) {
        return new EmailCodeVerificationVo(isVerified);
    }
}
