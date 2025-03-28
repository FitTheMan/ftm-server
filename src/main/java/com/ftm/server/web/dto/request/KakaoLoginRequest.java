package com.ftm.server.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginRequest {

    private final String authorizationCode;
}
