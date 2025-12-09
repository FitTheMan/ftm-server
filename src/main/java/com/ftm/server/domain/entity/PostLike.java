package com.ftm.server.domain.entity;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class PostLike extends BaseTime {
    private Long id;
    private Long post;
    private Long user;

    public static PostLike create(Long post, Long user) {
        return PostLike.builder().post(post).user(user).build();
    }

    public static PostLike create(Long id, Long post, Long user) {
        return PostLike.builder().id(id).post(post).user(user).build();
    }
}
