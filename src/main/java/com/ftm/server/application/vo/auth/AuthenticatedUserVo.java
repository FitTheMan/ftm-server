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
    private final String mildLevelName;
    private final String spicyLevelName;

    private AuthenticatedUserVo(User user, UserImage userImage, GroomingLevel groomingLevel) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImageUrl =
                PropertiesHolder.CDN_PATH
                        + "/"
                        + userImage
                                .getObjectKey(); // TODO: 추후 CDN 주소 + getObjectKey() 로 변경해야함 -> 변경완료
        this.mildLevelName = groomingLevel != null ? groomingLevel.getMildLevelName() : null;
        this.spicyLevelName = groomingLevel != null ? groomingLevel.getSpicyLevelName() : null;
    }

    //    public static AuthenticatedUserVo of(User user, UserImage userImage) {
    //        return new AuthenticatedUserVo(user, userImage, user.getGroomingLevel());
    //    }

    // 추후 수정 필요
    public static AuthenticatedUserVo of(User user, UserImage userImage) {
        return new AuthenticatedUserVo(user, userImage, null);
    }
}
