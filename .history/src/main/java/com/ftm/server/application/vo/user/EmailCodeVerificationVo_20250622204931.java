package com.ftm.server.application.vo.user;

import lombok.Data;

@Data
public class EmailCodeVerificationVo {
    private final Boolean isVerified;
    private final Boolean isRecoverable; // 계정 복구 가능 여부 (soft delete 상태인지)

    public static EmailCodeVerificationVo of(Boolean isVerified) {
        return new EmailCodeVerificationVo(isVerified, false);
    }

    public static EmailCodeVerificationVo of(Boolean isVerified, Boolean isRecoverable) {
        return new EmailCodeVerificationVo(isVerified, isRecoverable);
    }
}
