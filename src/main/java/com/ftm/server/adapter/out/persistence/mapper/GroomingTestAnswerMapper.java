package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.GroomingTestAnswerJpaEntity;
import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.GroomingTestAnswer;

@EntityMapper
public class GroomingTestAnswerMapper {

    public GroomingTestAnswer toDomain(GroomingTestAnswerJpaEntity jpaEntity) {
        return GroomingTestAnswer.of(
                jpaEntity.getId(),
                jpaEntity.getGroomingTestQuestion().getId(),
                jpaEntity.getAnswer(),
                jpaEntity.getScore(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public GroomingTestAnswerJpaEntity toJpaEntity(
            GroomingTestAnswer domainEntity,
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity) {
        return GroomingTestAnswerJpaEntity.from(domainEntity, groomingTestQuestionJpaEntity);
    }
}
