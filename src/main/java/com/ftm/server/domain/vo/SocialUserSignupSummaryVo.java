package com.ftm.server.domain.vo;

import com.ftm.server.common.consts.PropertiesHolder;
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
    private final String mildLevelName;
    private final String spicyLevelName;

    public static SocialUserSignupSummaryVo of(User user, String userImage) {
        String mildLevelName = null;
        String spicyLevelName = null;
        if (user.getGroomingLevel() != null) {
            mildLevelName = user.getGroomingLevel().getMildLevelName();
            spicyLevelName = user.getGroomingLevel().getMildLevelName();
        }
        return new SocialUserSignupSummaryVo(
                user,
                user.getId(),
                user.getNickname(),
                user.getSocialProvider(),
                PropertiesHolder.CDN_PATH + "/" + userImage,
                mildLevelName,
                spicyLevelName);
    }
}
