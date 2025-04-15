package com.ftm.server.application.vo.user;

import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import lombok.Data;

@Data
public class UserWithImageVo {
    private final User user;
    private final UserImage userImage;

    public static UserWithImageVo of(User user, UserImage userImage) {
        return new UserWithImageVo(user, userImage);
    }
}
