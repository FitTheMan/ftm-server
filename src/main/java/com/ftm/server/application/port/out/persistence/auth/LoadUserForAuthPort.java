package com.ftm.server.application.port.out.persistence.auth;

import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindBySocialValueQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.User;
import java.util.Optional;

@Port
public interface LoadUserForAuthPort {

    Optional<User> loadUserById(FindByIdQuery query);

    Optional<User> loadUserByEmail(FindByEmailQuery query);

    Optional<User> loadUserBySocialProviderAndSocialId(FindBySocialValueQuery query);
}
