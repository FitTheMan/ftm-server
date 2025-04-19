package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.UserImage;
import java.util.Optional;

@Port
public interface LoadUserImageForPostPort {

    Optional<UserImage> loadUserImageByUserId(FindByUserIdQuery query);
}
