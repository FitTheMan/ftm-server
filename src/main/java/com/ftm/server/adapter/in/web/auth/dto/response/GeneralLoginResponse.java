package com.ftm.server.adapter.in.web.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GeneralLoginResponse {

    private final Long id;
    private final String nickname;
    private final String profileImageUrl;
    private final String mildLevelName;
    private final String spicyLevelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime loginTime;

    GeneralLoginResponse(AuthenticatedUserVo authenticatedUserVo) {
        this.id = authenticatedUserVo.getId();
        this.nickname = authenticatedUserVo.getNickname();
        this.profileImageUrl = authenticatedUserVo.getProfileImageUrl();
        this.mildLevelName = authenticatedUserVo.getMildLevelName();
        this.spicyLevelName = authenticatedUserVo.getSpicyLevelName();
        this.loginTime = LocalDateTime.now();
    }

    public static GeneralLoginResponse from(AuthenticatedUserVo authenticatedUserVo) {
        return new GeneralLoginResponse(authenticatedUserVo);
    }
}
