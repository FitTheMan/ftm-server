package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindUserImagesByIdsQuery;
import com.ftm.server.domain.entity.UserImage;
import java.util.List;

public interface LoadUserImagePort {
    UserImage loadUserImageByUserId(FindByUserIdQuery query);

    List<UserImage> loadUserImagesByUserIdIn(FindUserImagesByIdsQuery query);
}
