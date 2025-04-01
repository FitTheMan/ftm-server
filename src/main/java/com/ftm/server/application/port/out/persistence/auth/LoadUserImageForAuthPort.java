package com.ftm.server.application.port.out.persistence.auth;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.UserImage;
import java.util.Optional;

@Port
public interface LoadUserImageForAuthPort {

    Optional<UserImage> loadUserImageByUserId(FindByUserIdQuery query);
}
