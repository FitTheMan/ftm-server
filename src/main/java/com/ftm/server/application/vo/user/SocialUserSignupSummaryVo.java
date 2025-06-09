package com.ftm.server.application.vo.user;

import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.SocialProvider;
import lombok.Data;

@Data
public class SocialUserSignupSummaryVo {
    private final User user;
    private final Long id;
    private final String nickname;
    private final SocialProvider socialProvider;
    private final String profileImageUrl;
    private final String normalLevelName;
    private final String truthLevelName;

    public static SocialUserSignupSummaryVo from(
            User user, String userImage, GroomingLevel groomingLevel) {
        String normalLevelName = groomingLevel != null ? groomingLevel.getNormalModeName() : null;
        String truthLevelName = groomingLevel != null ? groomingLevel.getTruthModeName() : null;

        return new SocialUserSignupSummaryVo(
                user,
                user.getId(),
                user.getNickname(),
                user.getSocialProvider(),
                PropertiesHolder.CDN_PATH + "/" + userImage,
                normalLevelName,
                truthLevelName);
    }
}
