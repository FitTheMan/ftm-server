package com.ftm.server.application.vo.auth;

import com.ftm.server.domain.entity.User;
import lombok.Getter;

/** 가입된 유저라 로그인에 성공한 경우 VO */
@Getter
public class SocialLoginSuccessVo extends SocialLoginOutcomeVo {

    private final User user;
    private final AuthenticatedUserVo authenticatedUserVo;

    private SocialLoginSuccessVo(User user, AuthenticatedUserVo authenticatedUserVo) {
        super(true);
        this.user = user;
        this.authenticatedUserVo = authenticatedUserVo;
    }

    public static SocialLoginSuccessVo from(User user, AuthenticatedUserVo authenticatedUserVo) {
        return new SocialLoginSuccessVo(user, authenticatedUserVo);
    }
}
