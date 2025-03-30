package com.ftm.server.application.dto.query;

import com.ftm.server.domain.enums.SocialProvider;
import lombok.Data;

@Data
public class FindBySocialValueQuery {

    private final SocialProvider socialProvider;
    private final String socialId;

    public static FindBySocialValueQuery of(SocialProvider socialProvider, String socialId) {
        return new FindBySocialValueQuery(socialProvider, socialId);
    }
}
