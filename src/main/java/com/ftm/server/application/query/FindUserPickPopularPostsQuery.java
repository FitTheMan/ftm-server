package com.ftm.server.application.query;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FindUserPickPopularPostsQuery {

    private final LocalDateTime since;
    private final Integer limit;

    public static FindUserPickPopularPostsQuery of(LocalDateTime since, Integer limit) {
        return new FindUserPickPopularPostsQuery(since, limit);
    }
}
