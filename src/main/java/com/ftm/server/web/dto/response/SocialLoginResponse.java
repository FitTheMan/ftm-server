package com.ftm.server.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.domain.enums.SocialProvider;
import com.ftm.server.domain.vo.UserSummaryVo;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SocialLoginResponse {

    private final Long id;
    private final String nickname;
    private final SocialProvider socialProvider;
    private final String profileImageUrl;
    private final String mildLevelName;
    private final String spicyLevelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime loginTime;

    SocialLoginResponse(SocialProvider socialProvider, UserSummaryVo userSummaryVo) {
        this.id = userSummaryVo.getId();
        this.nickname = userSummaryVo.getNickname();
        this.socialProvider = socialProvider;
        this.profileImageUrl = userSummaryVo.getProfileImageUrl();
        this.mildLevelName = userSummaryVo.getMildLevelName();
        this.spicyLevelName = userSummaryVo.getSpicyLevelName();
        this.loginTime = LocalDateTime.now();
    }

    public static SocialLoginResponse from(
            SocialProvider socialProvider, UserSummaryVo userSummaryVo) {
        return new SocialLoginResponse(socialProvider, userSummaryVo);
    }
}
