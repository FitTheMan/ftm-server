package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.domain.entity.User;

public interface LoadUserPort {
    User loadUserById(FindByUserIdQuery query);
}
