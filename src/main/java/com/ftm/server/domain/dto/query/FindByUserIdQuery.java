package com.ftm.server.domain.dto.query;

import lombok.Getter;

@Getter
public class FindByUserIdQuery {

    private final Long userId;

    private FindByUserIdQuery(Long userId) {
        this.userId = userId;
    }

    public static FindByUserIdQuery of(Long userId) {
        return new FindByUserIdQuery(userId);
    }
}
