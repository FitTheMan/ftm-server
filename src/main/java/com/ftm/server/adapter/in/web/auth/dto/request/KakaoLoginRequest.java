package com.ftm.server.adapter.in.web.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginRequest {

    private final String authorizationCode;
}
