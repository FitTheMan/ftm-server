package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.UserImage;

@Port
public interface SaveUserImagePort {
    void saveUserDefaultImage(UserImage userImage);
}
