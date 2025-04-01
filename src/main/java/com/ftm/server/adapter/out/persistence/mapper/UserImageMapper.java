package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.UserImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.UserImage;

@EntityMapper
public class UserImageMapper {

    public UserImage toDomainEntity(UserImageJpaEntity jpaEntity) {
        return UserImage.of(
                jpaEntity.getId(),
                jpaEntity.getUser().getId(),
                jpaEntity.getObjectKey(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public UserImageJpaEntity toJpaEntity(UserImage domainEntity, UserJpaEntity userJpaEntity) {
        return UserImageJpaEntity.from(domainEntity, userJpaEntity);
    }
}
