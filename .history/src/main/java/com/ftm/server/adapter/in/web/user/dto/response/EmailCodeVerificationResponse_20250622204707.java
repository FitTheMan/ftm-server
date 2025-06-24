package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.user.EmailCodeVerificationVo;
import lombok.Data;

@Data
public class EmailCodeVerificationResponse {
    private final Boolean isVerified;
    private final Boolean isRecoverable;

    public static EmailCodeVerificationResponse from(EmailCodeVerificationVo vo) {
        return new EmailCodeVerificationResponse(vo.getIsVerified(), vo.getIsRecoverable());
    }
}
