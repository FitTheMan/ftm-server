package com.ftm.server.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Builder(access = AccessLevel.PRIVATE)
    private PostLikeJpaEntity(PostJpaEntity post, UserJpaEntity user) {
        this.post = post;
        this.user = user;
    }

    public static PostLikeJpaEntity from(PostJpaEntity postJpaEntity, UserJpaEntity userJpaEntity) {
        return PostLikeJpaEntity.builder().post(postJpaEntity).user(userJpaEntity).build();
    }
}
