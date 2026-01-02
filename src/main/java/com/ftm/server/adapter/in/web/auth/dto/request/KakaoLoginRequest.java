package com.ftm.server.adapter.in.web.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginRequest {

    @NotBlank(message = "인가 코드는 필수 요청 정보입니다.")
    private final String authorizationCode;

    @NotBlank(message = "리다이렉트 키는 필수 요청 정보입니다.")
    @Pattern(regexp = "^(test|local|dev)$", message = "리다이렉트 키는 test, local, dev만 허용됩니다.")
    private final String redirectKey;
}
