package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.RecoverUserAccountCommand;
import com.ftm.server.application.vo.user.RecoverUserAccountVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface RecoverUserAccountUseCase {

    RecoverUserAccountVo execute(RecoverUserAccountCommand command);
}
