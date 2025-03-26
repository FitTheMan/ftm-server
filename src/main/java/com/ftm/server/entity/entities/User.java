package com.ftm.server.entity.entities;

import com.ftm.server.domain.dto.command.GeneralUserCreationCommand;
import com.ftm.server.entity.enums.AgeGroup;
import com.ftm.server.entity.enums.HashTag;
import com.ftm.server.entity.enums.SocialProvider;
import com.ftm.server.entity.enums.UserRole;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "age_group", columnDefinition = "age_group")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "social_provider", columnDefinition = "social_provider")
    private SocialProvider socialProvider;

    @Column(unique = true)
    private String socialId;

    @Column(nullable = false)
    private Integer groomingScore = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_level_id")
    private GroomingLevel groomingLevel;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", columnDefinition = "role")
    private UserRole role;

    @Type(
            value = EnumArrayType.class,
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = AbstractArrayType.SQL_ARRAY_TYPE,
                        value = "hashtag" // PostgreSQL에서 정의된 ENUM 타입 이름
                        )
            })
    @Column(
            name = "favorite_hashtags",
            columnDefinition = "hashtag[]" // PostgreSQL의 ENUM 배열 타입으로 지정
            )
    private HashTag[] favoriteHashtags;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private User(
            String email,
            String password,
            String nickname,
            AgeGroup ageGroup,
            SocialProvider socialProvider,
            String socialId,
            Integer groomingScore,
            GroomingLevel groomingLevel,
            UserRole role,
            HashTag[] favoriteHashtags,
            Boolean isDeleted,
            LocalDateTime deletedAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.groomingScore = groomingScore;
        this.groomingLevel = groomingLevel;
        this.role = role;
        this.favoriteHashtags = favoriteHashtags;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static User createGeneralUser(GeneralUserCreationCommand command) {
        return User.builder()
                .email(command.getEmail())
                .password(command.getPassword())
                .nickname(command.getNickName())
                .ageGroup(command.getAgeGroup())
                .favoriteHashtags(command.getHashTags())
                .groomingScore(0)
                .isDeleted(false)
                .role(UserRole.USER)
                .build();
    }
}
