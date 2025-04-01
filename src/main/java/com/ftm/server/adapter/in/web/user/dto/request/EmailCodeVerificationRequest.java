package com.ftm.server.adapter.in.web.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCodeVerificationRequest {

    @NotBlank private final String email;

    @NotBlank private final String code;
}
