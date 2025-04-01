package com.ftm.server.application.port.in.user;

import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.vo.user.EmailDuplicationVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface EmailDuplicationCheckUseCase {

    EmailDuplicationVo execute(FindByEmailQuery query);
}
