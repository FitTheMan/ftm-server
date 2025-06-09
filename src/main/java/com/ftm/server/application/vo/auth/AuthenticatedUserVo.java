package com.ftm.server.application.vo.auth;

import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import lombok.Getter;

@Getter
public class AuthenticatedUserVo {

    private final Long id;
    private final String nickname;
    private final String profileImageUrl;
    private final String normalLevelName;
    private final String truthLevelName;

    private AuthenticatedUserVo(User user, UserImage userImage, GroomingLevel groomingLevel) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImageUrl =
                PropertiesHolder.CDN_PATH
                        + "/"
                        + userImage
                                .getObjectKey(); // TODO: 추후 CDN 주소 + getObjectKey() 로 변경해야함 -> 변경완료
        this.normalLevelName = groomingLevel != null ? groomingLevel.getNormalModeName() : null;
        this.truthLevelName = groomingLevel != null ? groomingLevel.getTruthModeName() : null;
    }

    //    public static AuthenticatedUserVo of(User user, UserImage userImage) {
    //        return new AuthenticatedUserVo(user, userImage, user.getGroomingLevel());
    //    }

    // 추후 수정 필요
    public static AuthenticatedUserVo of(
            User user, UserImage userImage, GroomingLevel groomingLevel) {
        return new AuthenticatedUserVo(user, userImage, groomingLevel);
    }
}
