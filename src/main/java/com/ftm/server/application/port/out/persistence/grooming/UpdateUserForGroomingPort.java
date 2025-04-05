package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;

@Port
public interface UpdateUserForGroomingPort {

    void updateUserGroomingStatus(User user);
}
