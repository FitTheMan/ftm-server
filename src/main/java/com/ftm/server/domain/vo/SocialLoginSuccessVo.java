package com.ftm.server.domain.vo;

import com.ftm.server.domain.entity.User;
import lombok.Getter;

/** 가입된 유저라 로그인에 성공한 경우 VO */
@Getter
public class SocialLoginSuccessVo extends SocialLoginOutcomeVo {

    private final User user;
    private final UserSummaryVo userSummaryVo;

    private SocialLoginSuccessVo(User user, UserSummaryVo userSummaryVo) {
        super(true);
        this.user = user;
        this.userSummaryVo = userSummaryVo;
    }

    public static SocialLoginSuccessVo from(User user, UserSummaryVo userSummaryVo) {
        return new SocialLoginSuccessVo(user, userSummaryVo);
    }
}
