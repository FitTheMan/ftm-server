package com.ftm.server.domain.vo;

import com.ftm.server.domain.enums.SocialProvider;
import lombok.Getter;

/** 가입이 필요한 소셜 유저인 경우 VO */
@Getter
public class PendingSocialUserVo extends SocialLoginOutcomeVo {

    private final SocialProvider socialProvider;
    private final String socialId;

    private PendingSocialUserVo(SocialProvider socialProvider, String socialId) {
        super(false);
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }

    public static PendingSocialUserVo from(SocialProvider socialProvider, String socialId) {
        return new PendingSocialUserVo(socialProvider, socialId);
    }
}
