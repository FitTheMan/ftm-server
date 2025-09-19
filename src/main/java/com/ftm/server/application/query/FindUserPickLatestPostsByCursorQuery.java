package com.ftm.server.application.query;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUserPickLatestPostsByCursorQuery {
    private final Integer limit;
    private final LocalDateTime nextCursorCreatedAt;
    private final Long userId;

    public static FindUserPickLatestPostsByCursorQuery of(
            Integer limit, LocalDateTime nextCursorCreatedAt, Long userId) {
        return new FindUserPickLatestPostsByCursorQuery(limit, nextCursorCreatedAt, userId);
    }
}
