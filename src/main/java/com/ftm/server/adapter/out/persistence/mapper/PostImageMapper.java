package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.PostImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.PostImage;

@EntityMapper
public class PostImageMapper {

    public PostImage toDomainEntity(PostImageJpaEntity jpaEntity) {
        return PostImage.of(
                jpaEntity.getId(),
                jpaEntity.getPost().getId(),
                jpaEntity.getObjectKey(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public PostImageJpaEntity toJpaEntity(PostImage domainEntity, PostJpaEntity postJpaEntity) {
        return PostImageJpaEntity.from(domainEntity, postJpaEntity);
    }
}
