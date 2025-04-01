package com.ftm.server.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    @Builder(access = AccessLevel.PRIVATE)
    private BookmarkJpaEntity(UserJpaEntity user, PostJpaEntity post) {
        this.user = user;
        this.post = post;
    }

    public static BookmarkJpaEntity from(UserJpaEntity userJpaEntity, PostJpaEntity postJpaEntity) {
        return BookmarkJpaEntity.builder().user(userJpaEntity).post(postJpaEntity).build();
    }
}
