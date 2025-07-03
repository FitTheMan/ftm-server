package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.enums.PostHashtag;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Type(
            value = EnumArrayType.class,
            parameters =
                    @org.hibernate.annotations.Parameter(
                            name = AbstractArrayType.SQL_ARRAY_TYPE,
                            value = "post_hashtag"))
    @Column(name = "hashtags", columnDefinition = "post_hashtag[]")
    private PostHashtag[] hashtags;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private PostJpaEntity(
            UserJpaEntity user,
            String title,
            String content,
            PostHashtag[] hashtags,
            Integer viewCount,
            Integer likeCount,
            Boolean isDeleted,
            LocalDateTime deletedAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static PostJpaEntity from(Post post, UserJpaEntity userJpaEntity) {
        return PostJpaEntity.builder()
                .user(userJpaEntity)
                .title(post.getTitle())
                .content(post.getContent())
                .hashtags(post.getHashtags())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .isDeleted(post.getIsDeleted())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    public void updatePostForDomainEntity(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hashtags = post.getHashtags();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.isDeleted = post.getIsDeleted();
        this.deletedAt = post.getDeletedAt();
    }

    public void updatePostForDomainEntity(Post post, UserJpaEntity user) {
        this.title = post.getTitle();
        this.user = user;
        this.content = post.getContent();
        this.hashtags = post.getHashtags();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.isDeleted = post.getIsDeleted();
        this.deletedAt = post.getDeletedAt();
    }
}
