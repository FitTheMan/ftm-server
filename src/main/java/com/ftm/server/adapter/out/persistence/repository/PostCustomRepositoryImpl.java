package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QBookmarkJpaEntity.bookmarkJpaEntity;
import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.BookmarkYnWrapperVo;
import com.ftm.server.application.vo.post.PostIdAndBookmarkYnVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostJpaEntity> findAllByDeletedBefore(FindPostByDeleteOptionQuery query) {
        return queryFactory
                .selectFrom(postJpaEntity)
                .where(
                        postJpaEntity.isDeleted.eq(query.getIsDeleted()),
                        postJpaEntity.deletedAt.loe(query.getDeletedAt().atTime(LocalTime.MAX)))
                .fetch();
    }

    @Override
    public Slice<PostJpaEntity> findAllByUserIdWithPaging(FindPostsByPagingQuery query) {
        Pageable pageable = query.getPageable();
        List<PostJpaEntity> content =
                queryFactory
                        .selectFrom(postJpaEntity)
                        .where(postJpaEntity.user.id.eq(query.getUserId()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1) // 한 개 더 가져와서 hasNext 판별
                        .orderBy(postJpaEntity.createdAt.desc(), postJpaEntity.id.desc())
                        .fetch();

        List<PostJpaEntity> result = content;

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            result = content.subList(0, pageable.getPageSize()); // 초과분 제거
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public List<Tuple> findAllByCreatedDateInOneWeekAndUserGrouping(
            FindPostsByCreatedDateQuery query) {

        LocalDateTime oneWeekAgo =
                query.getDate().minusWeeks(1).atStartOfDay(); // 현재 기준 1주일 전 게시물만 조회

        return queryFactory
                .select(postJpaEntity.user, postJpaEntity.id.count())
                .from(postJpaEntity)
                .groupBy(postJpaEntity.user)
                .where(postJpaEntity.createdAt.goe(oneWeekAgo))
                .fetch();
    }

    @Override
    public List<BookmarkYnWrapperVo> findPostsByLatestCursor(
            FindUserPickLatestPostsByCursorQuery query) {

        BooleanBuilder condition = new BooleanBuilder();

        // 커서가 있으면 조건 추가
        if (query.getNextCursorCreatedAt() != null) {
            condition.and(
                    postJpaEntity.createdAt.lt(
                            query.getNextCursorCreatedAt()) // createdAt이 더 작은 게시글
                    );
        }

        if (query.getUserId() == null) {
            return queryFactory
                    .select(
                            Projections.constructor(
                                    BookmarkYnWrapperVo.class,
                                    Expressions.constant(false),
                                    postJpaEntity))
                    .from(postJpaEntity)
                    .where(condition)
                    .orderBy(
                            postJpaEntity.createdAt.desc(),
                            postJpaEntity.id.desc()) // 최신순 + tie-breaker
                    .limit(query.getLimit() + 1) // hasNext 체크를 위해 +1
                    .fetch();
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                BookmarkYnWrapperVo.class,
                                bookmarkJpaEntity.id.isNotNull(),
                                postJpaEntity))
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(
                        bookmarkJpaEntity
                                .post
                                .eq(postJpaEntity)
                                .and(
                                        bookmarkJpaEntity.user.id.eq(
                                                query.getUserId())) // 특정 user 북마크 여부 확인
                        )
                .where(condition)
                .orderBy(
                        postJpaEntity.createdAt.desc(),
                        postJpaEntity.id.desc()) // 최신순 + tie-breaker
                .limit(query.getLimit() + 1) // hasNext 체크를 위해 +1
                .fetch();
    }

    @Override
    public List<PostIdAndBookmarkYnVo> findPostIdWithBookmarkYn(FindByPostIdsAndUserQuery query) {

        List<PostIdAndBookmarkYnVo> temp =
                queryFactory
                        .select(
                                Projections.constructor(
                                        PostIdAndBookmarkYnVo.class,
                                        postJpaEntity.id,
                                        bookmarkJpaEntity.id.isNotNull()))
                        .from(postJpaEntity)
                        .leftJoin(bookmarkJpaEntity)
                        .on(
                                bookmarkJpaEntity
                                        .post
                                        .id
                                        .eq(postJpaEntity.id)
                                        .and(
                                                bookmarkJpaEntity.user.id.eq(
                                                        query.getUserId())) // 특정 user 북마크 여부 확인
                                )
                        .where(postJpaEntity.id.in(query.getPostIds()))
                        .fetch();

        return temp;
    }
}
