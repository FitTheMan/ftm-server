package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.User;
import lombok.RequiredArgsConstructor;

@EntityMapper
@RequiredArgsConstructor
public class UserMapper {

    public User toDomainEntity(UserJpaEntity jpaEntity) {
        return User.of(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getPassword(),
                jpaEntity.getNickname(),
                jpaEntity.getAgeGroup(),
                jpaEntity.getSocialProvider(),
                jpaEntity.getSocialId(),
                jpaEntity.getGroomingScore(),
                jpaEntity.getGroomingLevel() == null ? null : jpaEntity.getGroomingLevel().getId(),
                jpaEntity.getRole(),
                jpaEntity.getFavoriteHashtags(),
                jpaEntity.getIsDeleted(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public UserJpaEntity toJpaEntity(
            User domainEntity, GroomingLevelJpaEntity groomingLevelJpaEntity) {
        return UserJpaEntity.from(domainEntity, groomingLevelJpaEntity);
    }
}
