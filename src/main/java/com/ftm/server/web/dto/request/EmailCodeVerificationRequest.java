package com.ftm.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCodeVerificationRequest {

    @NotBlank private final String email;

    @NotBlank private final String code;
}
