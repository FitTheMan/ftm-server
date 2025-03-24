package com.ftm.server.adapter.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailAuthenticationRequest {

    @Pattern(
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식이 올바르지 않습니다.")
    private final String email;
}
