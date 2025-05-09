package com.ftm.server.application.query;

import lombok.Data;

@Data
public class FindBookmarkByUserIdAndPostIdQuery {
    private final Long userId;
    private final Long postId;

    public static FindBookmarkByUserIdAndPostIdQuery of(Long userId, Long postId) {
        return new FindBookmarkByUserIdAndPostIdQuery(userId, postId);
    }
}
