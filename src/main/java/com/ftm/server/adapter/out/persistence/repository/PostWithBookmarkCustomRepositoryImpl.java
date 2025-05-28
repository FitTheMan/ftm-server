package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QBookmarkJpaEntity.bookmarkJpaEntity;
import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostWithBookmarkCustomRepositoryImpl implements PostWithBookmarkCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostWithBookmarkCountVo> findAllPostsWithBookmarkCount(
            FindPostsByCreatedDateQuery query) {

        LocalDateTime twoWeeksAgo =
                query.getDate().minusWeeks(1).atStartOfDay(); // 현재 기준 1주일 전 게시물만 조회

        return queryFactory
                .select(
                        Projections.constructor(
                                PostWithBookmarkCountVo.class,
                                postJpaEntity.id,
                                postJpaEntity.title,
                                postJpaEntity.viewCount,
                                postJpaEntity.likeCount,
                                bookmarkJpaEntity.id.count()))
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.eq(postJpaEntity))
                .where(postJpaEntity.createdAt.goe(twoWeeksAgo))
                .groupBy(postJpaEntity.id)
                .fetch();
    }
}
