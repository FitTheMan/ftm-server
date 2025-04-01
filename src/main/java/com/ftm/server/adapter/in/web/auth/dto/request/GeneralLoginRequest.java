package com.ftm.server.adapter.in.web.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralLoginRequest {

    private final String email;
    private final String password;
}
