package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindUserByDeleteOptionQuery;
import com.ftm.server.application.query.FindUserByRoleQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;
import java.util.*;

@Port
public interface LoadUserPort {
    User loadUserById(FindByUserIdQuery query);

    User loadUserByRole(FindUserByRoleQuery query);

    List<User> loadUserByDeleteOption(FindUserByDeleteOptionQuery query);

    Optional<User> loadDeletedUserByEmail(FindByEmailQuery query);
}
