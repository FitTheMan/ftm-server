package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.domain.entity.User;

public interface SaveUserPort {
    User saveUser(User user);

    User saveSocialUser(User user);
}
