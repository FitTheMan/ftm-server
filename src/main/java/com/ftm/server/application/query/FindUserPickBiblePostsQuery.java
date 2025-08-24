package com.ftm.server.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUserPickBiblePostsQuery {

    private final Integer limit;

    public static FindUserPickBiblePostsQuery of(Integer limit) {
        return new FindUserPickBiblePostsQuery(limit);
    }
}
