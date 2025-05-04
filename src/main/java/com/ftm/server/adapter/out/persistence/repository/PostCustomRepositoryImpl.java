package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
}
