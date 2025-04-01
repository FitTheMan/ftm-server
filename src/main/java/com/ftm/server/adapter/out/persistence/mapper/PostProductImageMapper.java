package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.PostProductImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.PostProductImage;

@EntityMapper
public class PostProductImageMapper {

    public PostProductImage toDomainEntity(PostProductImageJpaEntity jpaEntity) {
        return PostProductImage.of(
                jpaEntity.getId(),
                jpaEntity.getPostProduct().getId(),
                jpaEntity.getObjectKey(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public PostProductImageJpaEntity toJpaEntity(
            PostProductImage domainEntity, PostProductJpaEntity postProductJpaEntity) {
        return PostProductImageJpaEntity.from(domainEntity, postProductJpaEntity);
    }
}
