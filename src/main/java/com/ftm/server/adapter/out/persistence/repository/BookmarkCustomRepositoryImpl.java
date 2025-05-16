package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QBookmarkJpaEntity.bookmarkJpaEntity;

import com.ftm.server.adapter.out.persistence.model.BookmarkJpaEntity;
import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<BookmarkJpaEntity> findAllByUserIdWithPaging(FindBookmarksByPagingQuery query) {
        Pageable pageable = query.getPageable();
        List<BookmarkJpaEntity> content =
                queryFactory
                        .selectFrom(bookmarkJpaEntity)
                        .where(bookmarkJpaEntity.user.id.eq(query.getUserId()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1) // 한 개 더 가져와서 hasNext 판별
                        .orderBy(bookmarkJpaEntity.createdAt.desc(), bookmarkJpaEntity.id.desc())
                        .fetch();

        List<BookmarkJpaEntity> result = content;

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            result = content.subList(0, pageable.getPageSize()); // 초과분 제거
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
