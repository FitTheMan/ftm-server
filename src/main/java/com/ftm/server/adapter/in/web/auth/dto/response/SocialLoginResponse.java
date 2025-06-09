package com.ftm.server.adapter.in.web.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import com.ftm.server.domain.enums.SocialProvider;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SocialLoginResponse {

    private final Long id;
    private final String nickname;
    private final SocialProvider socialProvider;
    private final String profileImageUrl;
    private final String normalLevelName;
    private final String truthLevelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime loginTime;

    SocialLoginResponse(SocialProvider socialProvider, AuthenticatedUserVo authenticatedUserVo) {
        this.id = authenticatedUserVo.getId();
        this.nickname = authenticatedUserVo.getNickname();
        this.socialProvider = socialProvider;
        this.profileImageUrl = authenticatedUserVo.getProfileImageUrl();
        this.normalLevelName = authenticatedUserVo.getNormalLevelName();
        this.truthLevelName = authenticatedUserVo.getTruthLevelName();
        this.loginTime = LocalDateTime.now();
    }

    public static SocialLoginResponse from(
            SocialProvider socialProvider, AuthenticatedUserVo authenticatedUserVo) {
        return new SocialLoginResponse(socialProvider, authenticatedUserVo);
    }
}
