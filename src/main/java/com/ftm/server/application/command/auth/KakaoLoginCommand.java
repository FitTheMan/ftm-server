package com.ftm.server.application.command.auth;

import com.ftm.server.adapter.in.web.auth.dto.request.KakaoLoginRequest;
import lombok.Getter;

@Getter
public class KakaoLoginCommand extends SocialLoginCommand {

    private KakaoLoginCommand(String authorizationCode) {
        super(authorizationCode);
    }

    public static KakaoLoginCommand from(KakaoLoginRequest request) {
        return new KakaoLoginCommand(request.getAuthorizationCode());
    }
}
