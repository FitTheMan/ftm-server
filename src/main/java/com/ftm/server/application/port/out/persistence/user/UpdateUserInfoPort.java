package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;

@Port
public interface UpdateUserInfoPort {
    void updateUserInfo(User user);
}
