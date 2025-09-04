package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QBookmarkJpaEntity.bookmarkJpaEntity;
import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
import com.ftm.server.application.vo.post.PostWithUserAndBookmarkCountVo;
import com.ftm.server.application.vo.post.UserWithPostCountVo;
import com.ftm.server.domain.enums.UserRole;
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
                                postJpaEntity.viewCount, // sum() 쓰지 마세요
                                postJpaEntity.likeCount, // sum() 쓰지 마세요
                                bookmarkJpaEntity.id.countDistinct() // 북마크 개수
                                ))
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.eq(postJpaEntity))
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
    public List<PostWithIdAndAuthorVo> findTopNPostsByBookmarkCount(int limit) {
        return queryFactory
                .select(Projections.constructor(PostWithIdAndAuthorVo.class, postJpaEntity.id))
                .from(postJpaEntity)
                .leftJoin(bookmarkJpaEntity)
                .on(bookmarkJpaEntity.post.eq(postJpaEntity))
                .where(postJpaEntity.isDeleted.eq(false))
                .groupBy(postJpaEntity.id)
                .orderBy(bookmarkJpaEntity.id.countDistinct().desc(), postJpaEntity.id.desc())
                .limit(limit)
                .fetch();
    }
}
