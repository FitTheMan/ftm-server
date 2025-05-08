package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import com.ftm.server.application.query.FindPostsByPagingQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                        .orderBy(postJpaEntity.createdAt.desc())
                        .fetch();

        List<PostJpaEntity> result = content;

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            result = content.subList(0, pageable.getPageSize()); // 초과분 제거
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
