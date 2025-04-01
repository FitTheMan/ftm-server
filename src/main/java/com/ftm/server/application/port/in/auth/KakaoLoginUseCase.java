package com.ftm.server.application.port.in.auth;

import com.ftm.server.application.command.auth.KakaoLoginCommand;
import com.ftm.server.application.vo.auth.SocialLoginOutcomeVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface KakaoLoginUseCase {

    SocialLoginOutcomeVo execute(KakaoLoginCommand command);
}
