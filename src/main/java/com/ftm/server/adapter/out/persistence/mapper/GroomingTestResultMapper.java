package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.GroomingTestAnswerJpaEntity;
import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import com.ftm.server.adapter.out.persistence.model.GroomingTestResultJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.GroomingTestResult;

@EntityMapper
public class GroomingTestResultMapper {

    public GroomingTestResult toDomainEntity(GroomingTestResultJpaEntity jpaEntity) {

        return GroomingTestResult.of(
                jpaEntity.getId(),
                jpaEntity.getUser().getId(),
                jpaEntity.getGroomingTestQuestion().getId(),
                jpaEntity.getGroomingTestAnswer().getId(),
                jpaEntity.getTestedAt(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public GroomingTestResultJpaEntity toJpaEntity(
            UserJpaEntity userJpaEntity,
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity,
            GroomingTestAnswerJpaEntity groomingTestAnswerJpaEntity,
            GroomingTestResult domainEntity) {
        return GroomingTestResultJpaEntity.from(
                userJpaEntity,
                groomingTestQuestionJpaEntity,
                groomingTestAnswerJpaEntity,
                domainEntity);
    }
}
