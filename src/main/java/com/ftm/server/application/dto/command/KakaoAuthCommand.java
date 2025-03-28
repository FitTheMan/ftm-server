package com.ftm.server.application.dto.command;

import com.ftm.server.web.dto.request.KakaoLoginRequest;
import lombok.Getter;

@Getter
public class KakaoAuthCommand extends SocialAuthCommand {

    private KakaoAuthCommand(String authorizationCode) {
        super(authorizationCode);
    }

    public static KakaoAuthCommand from(KakaoLoginRequest request) {
        return new KakaoAuthCommand(request.getAuthorizationCode());
    }
}
