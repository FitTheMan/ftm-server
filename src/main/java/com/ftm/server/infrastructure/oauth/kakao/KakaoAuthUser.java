package com.ftm.server.infrastructure.oauth.kakao;

import com.ftm.server.domain.enums.SocialProvider;
import com.ftm.server.infrastructure.oauth.SocialAuthUser;
import lombok.Getter;

@Getter
public class KakaoAuthUser extends SocialAuthUser {

    private KakaoAuthUser(String socialId) {
        super(SocialProvider.KAKAO, socialId);
    }

    public static KakaoAuthUser from(String socialId) {
        return new KakaoAuthUser(socialId);
    }
}
