package com.ftm.server.adapter.in.web.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.user.SocialUserSignupSummaryVo;
import com.ftm.server.domain.enums.SocialProvider;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SocialUserSignupResponse {

    private final Long id;
    private final String nickname;
    private final SocialProvider socialProvider;
    private final String profileImageUrl;
    private final String normalLevelName;
    private final String truthLevelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime loginTime;

    public static SocialUserSignupResponse from(SocialUserSignupSummaryVo vo) {
        return new SocialUserSignupResponse(
                vo.getId(),
                vo.getNickname(),
                vo.getSocialProvider(),
                vo.getProfileImageUrl(),
                vo.getNormalLevelName(),
                vo.getTruthLevelName(),
                LocalDateTime.now());
    }
}
