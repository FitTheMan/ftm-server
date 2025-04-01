package com.ftm.server.application.query;

import com.ftm.server.domain.enums.SocialProvider;
import lombok.Data;

@Data
public class FindBySocialValueQuery {

    private final SocialProvider socialProvider;
    private final String socialId;

    private FindBySocialValueQuery(SocialProvider socialProvider, String socialId) {
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }

    public static FindBySocialValueQuery of(SocialProvider socialProvider, String socialId) {
        return new FindBySocialValueQuery(socialProvider, socialId);
    }
}
