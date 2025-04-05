package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;
import java.util.Optional;

@Port
public interface LoadUserForGroomingPort {

    Optional<User> loadUser(FindByIdQuery query);
}
