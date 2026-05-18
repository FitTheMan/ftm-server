package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QBookmarkJpaEntity.bookmarkJpaEntity;
import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.domain.enums.UserRole;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
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

        LocalDateTime oneWeekAgo =
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
                .where(postJpaEntity.createdAt.goe(oneWeekAgo))
                .where(postJpaEntity.isDeleted.eq(false)) // 삭제 되지 않은 것만 트렌딩 게시물에 포함하기
                .groupBy(postJpaEntity.id)
                .fetch();
    }

    @Override
    public List<UserWithPostCountVo> findAllPostsWithUserAndBookmarkCount(
            FindPostsByCreatedDateQuery query) {
        LocalDateTime oneWeekAgo =
                query.getDate().minusWeeks(1).atStartOfDay(); // 현재 기준 1주일 전 게시물만 조회

        return queryFactory
                .select(
                        Projections.constructor(
                                UserWithPostCountVo.class,
                                postJpaEntity.user.id,
                                postJpaEntity.user.nickname,
                                postJpaEntity.viewCount.sum(),
                                postJpaEntity.likeCount.sum(),
                                bookmarkJpaEntity.id.count()))
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.eq(postJpaEntity))
                .where(
                        postJpaEntity
                                .isDeleted
                                .eq(false)
                                .and(
                                        postJpaEntity.createdAt.goe(
                                                oneWeekAgo))) // 삭제되지 않은 1주일 이내 게시물만 포함.
                .where(
                        postJpaEntity
                                .user
                                .isDeleted
                                .eq(false)
                                .and(
                                        postJpaEntity.user.role.eq(
                                                UserRole.USER))) // 삭제되지 않은 일반 유저만 포함
                .groupBy(postJpaEntity.user.id, postJpaEntity.user.nickname)
                .fetch();
    }

    @Override
    public List<PostWithUserAndBookmarkCountVo> findAllPostsWithUserAndBookmarkCount(
            FindByIdsQuery query) {
        return queryFactory
                .select(
                        Projections.constructor(
                                PostWithUserAndBookmarkCountVo.class,
                                postJpaEntity.id,
                                postJpaEntity.user.id,
                                postJpaEntity.user.nickname,
                                postJpaEntity.title,
                                postJpaEntity.content,
                                postJpaEntity.hashtags,
                                postJpaEntity.viewCount,
                                postJpaEntity.likeCount,
                                bookmarkJpaEntity.id.countDistinct() // 북마크 개수
                                ))
                .from(postJpaEntity)
                .where(postJpaEntity.id.in(query.getIds()))
                .where(postJpaEntity.isDeleted.eq(false))
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.id.eq(postJpaEntity.id))
                .groupBy(
                        postJpaEntity.id,
                        postJpaEntity.user.id,
                        postJpaEntity.user.nickname,
                        postJpaEntity.title,
                        postJpaEntity.content,
                        postJpaEntity.hashtags,
                        postJpaEntity.viewCount,
                        postJpaEntity.likeCount)
                .fetch();
    }

    @Override
    public List<Long> findTopNPostsByBookmarkCount(int limit) {
        return queryFactory
                .select(postJpaEntity.id)
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.eq(postJpaEntity))
                .where(postJpaEntity.isDeleted.eq(false))
                .groupBy(postJpaEntity.id)
                .orderBy(bookmarkJpaEntity.id.countDistinct().desc(), postJpaEntity.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<UserPickPopularPostCursorVo> findAllPostsByPopular() {

        Tuple maxValues =
                queryFactory
                        .select(postJpaEntity.likeCount.max(), postJpaEntity.viewCount.max())
                        .from(postJpaEntity)
                        .where(postJpaEntity.isDeleted.eq(false))
                        .fetchOne();

        int maxLike = maxValues.get(postJpaEntity.likeCount.max());
        int maxView = maxValues.get(postJpaEntity.viewCount.max());

        List<UserPickPopularPostCursorVo> result =
                queryFactory
                        .select(postJpaEntity)
                        .from(postJpaEntity)
                        .where(postJpaEntity.isDeleted.eq(false))
                        .fetch()
                        .stream()
                        .map(
                                vo -> {
                                    double normLike =
                                            maxLike > 0 ? vo.getLikeCount() / (double) maxLike : 0;
                                    double normView =
                                            maxView > 0 ? vo.getViewCount() / (double) maxView : 0;
                                    double weighted = normLike * 0.6 + normView * 0.4;

                                    long hours =
                                            Duration.between(vo.getCreatedAt(), LocalDateTime.now())
                                                    .toHours();
                                    double timeDecay = Math.exp(-0.1 * hours);

                                    return new UserPickPopularPostCursorVo(
                                            vo.getId(), weighted * timeDecay);
                                })
                        .sorted(
                                Comparator.comparing(UserPickPopularPostCursorVo::getScore)
                                        .reversed())
                        .toList();
        return result;
    }
}
