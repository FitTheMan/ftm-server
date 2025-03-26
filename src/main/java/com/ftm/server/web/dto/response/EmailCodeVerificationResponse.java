package com.ftm.server.web.dto.response;

import com.ftm.server.domain.vo.EmailCodeVerificationVo;
import lombok.Data;

@Data
public class EmailCodeVerificationResponse {
    private final Boolean isVerified;

    public static EmailCodeVerificationResponse from(EmailCodeVerificationVo vo) {
        return new EmailCodeVerificationResponse(vo.getIsVerified());
    }
}
