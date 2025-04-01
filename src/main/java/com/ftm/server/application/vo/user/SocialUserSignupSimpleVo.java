package com.ftm.server.application.vo.user;

import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.User;
import lombok.Data;

@Data
public class SocialUserSignupSimpleVo {
    private final User user;
    private final GroomingLevel groomingLevel;

    public static SocialUserSignupSimpleVo from(User user, GroomingLevel groomingLevel) {
        return new SocialUserSignupSimpleVo(user, groomingLevel);
    }
}
