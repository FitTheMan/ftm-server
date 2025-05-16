package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.BookmarkJpaEntity;
import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import org.springframework.data.domain.Slice;

public interface BookmarkCustomRepository {

    Slice<BookmarkJpaEntity> findAllByUserIdWithPaging(FindBookmarksByPagingQuery query);
}
