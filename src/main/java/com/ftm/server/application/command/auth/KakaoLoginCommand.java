package com.ftm.server.application.command.auth;

import com.ftm.server.adapter.in.web.auth.dto.request.KakaoLoginRequest;
import com.ftm.server.domain.enums.RedirectEnv;
import lombok.Getter;

@Getter
public class KakaoLoginCommand extends SocialLoginCommand {

    private final RedirectEnv redirectEnv;

    private KakaoLoginCommand(String authorizationCode, String redirectKey) {
        super(authorizationCode);
        this.redirectEnv = RedirectEnv.from(redirectKey);
    }

    public static KakaoLoginCommand from(KakaoLoginRequest request) {
        return new KakaoLoginCommand(request.getAuthorizationCode(), request.getRedirectKey());
    }
}
