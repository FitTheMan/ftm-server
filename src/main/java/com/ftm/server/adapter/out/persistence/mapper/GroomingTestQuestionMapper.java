package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.GroomingTestQuestion;

@EntityMapper
public class GroomingTestQuestionMapper {

    public GroomingTestQuestion toDomain(GroomingTestQuestionJpaEntity jpaEntity) {
        return GroomingTestQuestion.of(
                jpaEntity.getId(),
                jpaEntity.getGroomingCategory(),
                jpaEntity.getQuestion(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public GroomingTestQuestionJpaEntity toJpaEntity(GroomingTestQuestion domainEntity) {
        return GroomingTestQuestionJpaEntity.from(domainEntity);
    }
}
