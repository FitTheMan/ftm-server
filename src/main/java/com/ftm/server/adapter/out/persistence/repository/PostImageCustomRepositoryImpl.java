package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QPostImageJpaEntity.postImageJpaEntity;

import com.ftm.server.adapter.out.persistence.model.PostImageJpaEntity;
import com.ftm.server.application.query.FindByIdsQuery;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostImageCustomRepositoryImpl implements PostImageCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostImageJpaEntity> findRepresentativeImagesByPostIdIn(FindByIdsQuery query) {
        return queryFactory
                .selectFrom(postImageJpaEntity)
                .where(
                        postImageJpaEntity.id.in(
                                JPAExpressions.select(postImageJpaEntity.id.min())
                                        .from(postImageJpaEntity)
                                        .where(postImageJpaEntity.post.id.in(query.getIds()))
                                        .groupBy(postImageJpaEntity.post.id)))
                .fetch();
    }
}
