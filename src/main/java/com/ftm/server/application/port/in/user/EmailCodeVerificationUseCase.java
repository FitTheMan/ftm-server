package com.ftm.server.application.port.in.user;

import com.ftm.server.application.query.EmailCodeVerificationQuery;
import com.ftm.server.application.vo.user.EmailCodeVerificationVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface EmailCodeVerificationUseCase {
    EmailCodeVerificationVo execute(EmailCodeVerificationQuery query);
}
