package com.ftm.server.domain.entity;

import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, name = "grooming_category", columnDefinition = "grooming_category")
    private GroomingCategory groomingCategory;

    @Type(
            value = EnumArrayType.class,
            parameters =
                    @org.hibernate.annotations.Parameter(
                            name = AbstractArrayType.SQL_ARRAY_TYPE,
                            value = "hashtag"))
    @Column(name = "hashtags", columnDefinition = "hashtag[]")
    private HashTag[] hashtags;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Post(
            User user,
            String title,
            String content,
            GroomingCategory groomingCategory,
            HashTag[] hashtags,
            Integer viewCount,
            Integer likeCount,
            Boolean isDeleted,
            LocalDateTime deletedAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.groomingCategory = groomingCategory;
        this.hashtags = hashtags;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }
}
