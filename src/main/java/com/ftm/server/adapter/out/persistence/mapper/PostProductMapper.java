package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.PostProduct;

@EntityMapper
public class PostProductMapper {

    public PostProduct toDomainEntity(PostProductJpaEntity jpaEntity) {
        return PostProduct.of(
                jpaEntity.getId(),
                jpaEntity.getPost().getId(),
                jpaEntity.getName(),
                jpaEntity.getBrand(),
                jpaEntity.getHashtags(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public PostProductJpaEntity toJpaEntity(PostProduct domainEntity, PostJpaEntity postJpaEntity) {
        return PostProductJpaEntity.from(domainEntity, postJpaEntity);
    }
}
