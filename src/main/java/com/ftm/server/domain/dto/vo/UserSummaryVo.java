package com.ftm.server.domain.dto.vo;

import com.ftm.server.entity.entities.GroomingLevel;
import com.ftm.server.entity.entities.User;
import com.ftm.server.entity.entities.UserImage;
import lombok.Getter;

@Getter
public class UserSummaryVo {

    private final Long id;
    private final String nickname;
    private final String profileImageUrl;
    private final String mildLevelName;
    private final String spicyLevelName;

    private UserSummaryVo(User user, UserImage userImage, GroomingLevel groomingLevel) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImageUrl = userImage.getObjectKey(); // TODO: 추후 CDN 주소 + getObjectKey() 로 변경해야함
        this.mildLevelName = groomingLevel.getMildLevelName();
        this.spicyLevelName = groomingLevel.getSpicyLevelName();
    }

    public static UserSummaryVo of(User user, UserImage userImage) {
        return new UserSummaryVo(user, userImage, user.getGroomingLevel());
    }
}
