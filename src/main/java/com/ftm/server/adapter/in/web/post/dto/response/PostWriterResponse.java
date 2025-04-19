package com.ftm.server.adapter.in.web.post.dto.response;

import static com.ftm.server.common.consts.PropertiesHolder.CDN_PATH;

import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import lombok.Getter;

@Getter
public class PostWriterResponse {

    private final Long userId;
    private final String nickname;
    private final String imageUrl;

    private PostWriterResponse(User user, UserImage userImage) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.imageUrl = CDN_PATH + "/" + userImage.getObjectKey();
    }

    public static PostWriterResponse from(User user, UserImage userImage) {
        return new PostWriterResponse(user, userImage);
    }
}
