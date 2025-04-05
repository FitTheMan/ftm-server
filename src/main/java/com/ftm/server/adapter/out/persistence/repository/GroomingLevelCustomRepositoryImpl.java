package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QGroomingLevelJpaEntity.groomingLevelJpaEntity;

import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroomingLevelCustomRepositoryImpl implements GroomingLevelCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GroomingLevelJpaEntity> findByScoreInRange(int score) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(groomingLevelJpaEntity)
                        .where(
                                groomingLevelJpaEntity.minScore.loe(score),
                                groomingLevelJpaEntity.maxScore.goe(score))
                        .fetchOne());
    }
}
