package com.ftm.server.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUserPickPopularPostsByCursorQuery {

    private Integer size;
    private Double lastScore; // nullable
    private Long lastId; // nullable
    private final Long userId;

    public static FindUserPickPopularPostsByCursorQuery of(
            Integer size, Double lastScore, Long lastId, Long userId) {
        return new FindUserPickPopularPostsByCursorQuery(size, lastScore, lastId, userId);
    }
}
