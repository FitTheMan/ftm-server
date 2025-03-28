package com.ftm.server.infrastructure.oauth;

import com.ftm.server.domain.enums.SocialProvider;
import lombok.Getter;

@Getter
public abstract class SocialAuthUser {

    private final SocialProvider socialProvider;
    private final String socialId;

    protected SocialAuthUser(SocialProvider socialProvider, String socialId) {
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }
}
