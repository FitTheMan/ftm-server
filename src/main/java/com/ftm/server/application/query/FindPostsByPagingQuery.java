package com.ftm.server.application.query;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
public class FindPostsByPagingQuery {

    private final Long userId;
    private final Pageable pageable;

    private FindPostsByPagingQuery(Long userId, int page, int size) {
        this.userId = userId;
        this.pageable = PageRequest.of(page, size);
    }

    public static FindPostsByPagingQuery of(Long userId, int page, int size) {
        return new FindPostsByPagingQuery(userId, page, size);
    }
}
