package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.SocialUserSignupCommand;
import com.ftm.server.application.vo.user.SocialUserSignupSummaryVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SocialUserSignupUseCase {

    SocialUserSignupSummaryVo execute(SocialUserSignupCommand command);
}
