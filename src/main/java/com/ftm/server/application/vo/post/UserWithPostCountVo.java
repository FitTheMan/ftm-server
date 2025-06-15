package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserWithPostCountVo {
    private final Long userId;
    private final String userName;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;

    public static UserWithPostCountVo of(
            Long userId, String userName, Integer viewCount, Integer likeCount, Long scrapCount) {
        return new UserWithPostCountVo(userId, userName, viewCount, likeCount, scrapCount);
    }
}
