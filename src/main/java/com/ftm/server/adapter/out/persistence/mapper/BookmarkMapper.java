package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.BookmarkJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.Bookmark;

@EntityMapper
public class BookmarkMapper {

    public Bookmark toDomainEntity(BookmarkJpaEntity jpaEntity) {
        return Bookmark.of(
                jpaEntity.getId(),
                jpaEntity.getUser().getId(),
                jpaEntity.getPost().getId(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public BookmarkJpaEntity toJpaEntity(UserJpaEntity userJpaEntity, PostJpaEntity postJpaEntity) {
        return BookmarkJpaEntity.from(userJpaEntity, postJpaEntity);
    }
}
