package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.GroomingLevel;

@EntityMapper
public class GroomingLevelMapper {

    public GroomingLevel toDomainEntity(GroomingLevelJpaEntity jpaEntity) {

        return GroomingLevel.of(
                jpaEntity.getId(),
                jpaEntity.getMinScore(),
                jpaEntity.getMaxScore(),
                jpaEntity.getNormalModeName(),
                jpaEntity.getNormalModeSummary(),
                jpaEntity.getNormalModeDescription(),
                jpaEntity.getTruthModeName(),
                jpaEntity.getTruthModeSummary(),
                jpaEntity.getTruthModeDescription(),
                jpaEntity.getImagePath(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public GroomingLevelJpaEntity toJpaEntity(GroomingLevel domainEntity) {
        return GroomingLevelJpaEntity.from(domainEntity);
    }
}
