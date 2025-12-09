package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QPostJpaEntity.postJpaEntity;
import static com.ftm.server.adapter.out.persistence.model.QPostProductJpaEntity.postProductJpaEntity;

import com.ftm.server.application.vo.post.ProductIdAndScoreVo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostProductCustomRepositoryImpl implements PostProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductIdAndScoreVo> findAllByPopularity() {

        Tuple maxValues =
                queryFactory
                        .select(
                                postJpaEntity.viewCount.max(),
                                postProductJpaEntity.recommendedCount.max())
                        .from(postProductJpaEntity)
                        .join(postProductJpaEntity.post, postJpaEntity)
                        .fetchOne();

        if (maxValues == null) {
            // 데이터 자체가 없으면 빈 리스트 반환
            return List.of();
        }

        Integer maxView = maxValues.get(postJpaEntity.viewCount.max());
        maxView = maxView == null || maxView == 0 ? 1 : maxView;
        Integer maxLike =
                maxValues.get(postProductJpaEntity.recommendedCount.max()) == null
                        ? 0
                        : maxValues.get(postProductJpaEntity.recommendedCount.max()).intValue();
        maxLike = maxLike == 0 ? 1 : maxLike;

        NumberExpression<Double> normalizedScore =
                postJpaEntity
                        .viewCount
                        .divide(maxView)
                        .doubleValue()
                        .add(postProductJpaEntity.recommendedCount.divide(maxLike).doubleValue());

        return queryFactory
                .select(
                        Projections.constructor(
                                ProductIdAndScoreVo.class,
                                postProductJpaEntity.id,
                                normalizedScore))
                .from(postProductJpaEntity)
                .join(postProductJpaEntity.post, postJpaEntity)
                .orderBy(normalizedScore.desc())
                .fetch();
    }
}
