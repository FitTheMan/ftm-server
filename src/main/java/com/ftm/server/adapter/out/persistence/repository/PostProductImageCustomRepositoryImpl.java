package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QPostProductImageJpaEntity.postProductImageJpaEntity;

import com.ftm.server.adapter.out.persistence.model.PostProductImageJpaEntity;
import com.ftm.server.application.query.FindByPostProductIdsQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostProductImageCustomRepositoryImpl implements PostProductImageCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostProductImageJpaEntity> findByPostProductIds(FindByPostProductIdsQuery query) {
        return queryFactory
                .selectFrom(postProductImageJpaEntity)
                .where(postProductImageJpaEntity.postProduct.id.in(query.getPostProductIds()))
                .fetch();
    }
}
