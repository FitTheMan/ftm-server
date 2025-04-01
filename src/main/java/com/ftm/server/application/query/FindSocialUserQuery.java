package com.ftm.server.application.query;

import com.ftm.server.domain.enums.SocialProvider;
import lombok.Getter;

@Getter
public class FindSocialUserQuery {

    private final SocialProvider socialProvider;
    private final String socialId;

    private FindSocialUserQuery(SocialProvider socialProvider, String socialId) {
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }

    public static FindSocialUserQuery of(SocialProvider socialProvider, String socialId) {
        return new FindSocialUserQuery(socialProvider, socialId);
    }
}
