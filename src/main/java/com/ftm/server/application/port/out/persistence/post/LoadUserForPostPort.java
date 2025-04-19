package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;
import java.util.Optional;

@Port
public interface LoadUserForPostPort {

    Optional<User> loadUserById(FindByIdQuery query);
}
