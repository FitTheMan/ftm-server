package com.ftm.server.application.query;

import lombok.Getter;

@Getter
public class FindByPostIdQuery {

    private final Long postId;

    private FindByPostIdQuery(Long postId) {
        this.postId = postId;
    }

    public static FindByPostIdQuery of(Long postId) {
        return new FindByPostIdQuery(postId);
    }
}
