package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.Post;

@EntityMapper
public class PostMapper {

    public Post toDomainEntity(PostJpaEntity jpaEntity) {
        return Post.of(
                jpaEntity.getId(),
                jpaEntity.getUser().getId(),
                jpaEntity.getTitle(),
                jpaEntity.getContent(),
                jpaEntity.getHashtags(),
                jpaEntity.getViewCount(),
                jpaEntity.getLikeCount(),
                jpaEntity.getIsDeleted(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public PostJpaEntity toJpaEntity(Post domainEntity, UserJpaEntity userJpaEntity) {
        return PostJpaEntity.from(domainEntity, userJpaEntity);
    }
}
