package com.ftm.server.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindTopPostsByBookmarkCountQuery {
    private final int limit;

    public static FindTopPostsByBookmarkCountQuery of(int limit) {
        return new FindTopPostsByBookmarkCountQuery(limit);
    }
}
