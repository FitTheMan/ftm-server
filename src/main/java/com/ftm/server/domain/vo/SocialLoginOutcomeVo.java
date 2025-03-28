package com.ftm.server.domain.vo;

import java.io.Serializable;
import lombok.Getter;

/** 소셜 로그인 결과 VO */
@Getter
public abstract class SocialLoginOutcomeVo implements Serializable {

    private final boolean registered;

    protected SocialLoginOutcomeVo(boolean registered) {
        this.registered = registered;
    }
}
