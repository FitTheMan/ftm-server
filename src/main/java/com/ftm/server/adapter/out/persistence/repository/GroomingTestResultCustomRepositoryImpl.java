package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QGroomingTestResultJpaEntity.groomingTestResultJpaEntity;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroomingTestResultCustomRepositoryImpl implements GroomingTestResultCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public LocalDateTime fetchLatestTestedAtByUserId(FindByUserIdQuery query) {
        return queryFactory
                .select(groomingTestResultJpaEntity.testedAt.max())
                .from(groomingTestResultJpaEntity)
                .where(groomingTestResultJpaEntity.user.id.eq(query.getUserId()))
                .fetchOne();
    }
}
