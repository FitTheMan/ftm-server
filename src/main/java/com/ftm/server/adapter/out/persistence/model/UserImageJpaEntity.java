package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.UserImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImageJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Column(nullable = false)
    private String objectKey = "default-image";

    @Builder(access = AccessLevel.PRIVATE)
    private UserImageJpaEntity(UserJpaEntity user, String objectKey) {
        this.user = user;
        this.objectKey = objectKey;
    }

    public static UserImageJpaEntity from(UserImage userImage, UserJpaEntity userJpaEntity) {
        return UserImageJpaEntity.builder()
                .user(userJpaEntity)
                .objectKey(userImage.getObjectKey())
                .build();
    }

    public static UserImageJpaEntity createUserImage(UserJpaEntity user) {
        return UserImageJpaEntity.builder().user(user).objectKey("users/default-image.png").build();
    }

    public void updateFromDomainEntity(UserImage userImage) {
        this.objectKey = userImage.getObjectKey();
    }
}
