package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.UpdateUserCommand;
import com.ftm.server.application.vo.user.UserWithImageVo;
import jakarta.transaction.Transactional;

public interface UpdateUserInfoUseCase {
    @Transactional
    UserWithImageVo execute(UpdateUserCommand updateUserCommand);
}
