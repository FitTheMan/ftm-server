package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.BeautyProductJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.BeautyProduct;

@EntityMapper
public class BeautyProductMapper {

    public BeautyProduct toDomainEntity(BeautyProductJpaEntity jpaEntity) {
        return BeautyProduct.of(
                jpaEntity.getId(),
                jpaEntity.getProductImageLink(),
                jpaEntity.getBeautyProductCategory(),
                jpaEntity.getBrand(),
                jpaEntity.getName(),
                jpaEntity.getRating(),
                jpaEntity.getProductPageLink(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public BeautyProductJpaEntity toJpaEntity(BeautyProduct domainEntity) {
        return BeautyProductJpaEntity.from(domainEntity);
    }
}
