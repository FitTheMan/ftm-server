package com.ftm.server.application.query;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
public class FindBookmarksByPagingQuery {

    private final Long userId;
    private final Pageable pageable;

    private FindBookmarksByPagingQuery(Long userId, int page, int size) {
        this.userId = userId;
        this.pageable = PageRequest.of(page, size);
    }

    public static FindBookmarksByPagingQuery of(Long userId, int page, int size) {
        return new FindBookmarksByPagingQuery(userId, page, size);
    }
}
