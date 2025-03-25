package com.ftm.server.domain.dto.query;

import com.ftm.server.adapter.dto.request.EmailCodeVerificationRequest;
import lombok.Data;

@Data
public class EmailCodeVerificationQuery {
    private final String code;
    private final String email;

    public static EmailCodeVerificationQuery from(EmailCodeVerificationRequest request) {
        return new EmailCodeVerificationQuery(request.getCode(), request.getEmail());
    }
}
