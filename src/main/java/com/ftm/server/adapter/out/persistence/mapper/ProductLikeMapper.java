package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import com.ftm.server.adapter.out.persistence.model.ProductLikeJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.ProductLike;

@EntityMapper
public class ProductLikeMapper {
    public ProductLike toDomainEntity(ProductLikeJpaEntity jpaEntity) {
        return ProductLike.create(
                jpaEntity.getId(), jpaEntity.getPostProduct().getId(), jpaEntity.getUser().getId());
    }

    public ProductLikeJpaEntity toJpaEntity(
            PostProductJpaEntity postProductJpaEntity, UserJpaEntity userJpaEntity) {
        return ProductLikeJpaEntity.from(postProductJpaEntity, userJpaEntity);
    }
}
