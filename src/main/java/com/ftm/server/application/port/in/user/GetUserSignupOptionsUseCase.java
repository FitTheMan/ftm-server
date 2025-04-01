package com.ftm.server.application.port.in.user;

import com.ftm.server.application.vo.user.UserSignupOptionsVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface GetUserSignupOptionsUseCase {

    UserSignupOptionsVo execute();
}
