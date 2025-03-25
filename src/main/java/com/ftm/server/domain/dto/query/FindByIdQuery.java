package com.ftm.server.domain.dto.query;

import lombok.Getter;

@Getter
public class FindByIdQuery {

    private final Long id;

    private FindByIdQuery(Long id) {
        this.id = id;
    }

    public static FindByIdQuery of(Long id) {
        return new FindByIdQuery(id);
    }
}
