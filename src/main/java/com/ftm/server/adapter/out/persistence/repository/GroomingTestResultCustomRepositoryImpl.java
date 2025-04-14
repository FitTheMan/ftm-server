package com.ftm.server.adapter.out.persistence.repository;

import static com.ftm.server.adapter.out.persistence.model.QGroomingTestResultJpaEntity.groomingTestResultJpaEntity;

import com.ftm.server.adapter.out.persistence.model.GroomingTestResultJpaEntity;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

    @Override
    public List<LocalDateTime> fetchRecentTestedAtListByUserId(FindByUserIdQuery query) {
        return queryFactory
                .select(groomingTestResultJpaEntity.testedAt)
                .distinct()
                .from(groomingTestResultJpaEntity)
                .where(groomingTestResultJpaEntity.user.id.eq(query.getUserId()))
                .orderBy(groomingTestResultJpaEntity.testedAt.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<GroomingTestResultJpaEntity> loadByUserIdAndTestedAt(
            FindGroomingTestResultByUserIdAndTestedAtQuery query) {
        LocalDateTime start = query.getTestedAt().atStartOfDay();
        LocalDateTime end = query.getTestedAt().atTime(LocalTime.MAX);

        return queryFactory
                .selectFrom(groomingTestResultJpaEntity)
                .where(
                        groomingTestResultJpaEntity
                                .user
                                .id
                                .eq(query.getUserId())
                                .and(groomingTestResultJpaEntity.testedAt.between(start, end)))
                .fetch();
    }
}
