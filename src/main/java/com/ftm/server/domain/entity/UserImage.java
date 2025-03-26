package com.ftm.server.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String objectKey = "default-image";

    @Builder(access = AccessLevel.PRIVATE)
    private UserImage(User user, String objectKey) {
        this.user = user;
        this.objectKey = objectKey;
    }

    public static UserImage createUserImage(User user) {
        return UserImage.builder().user(user).objectKey("users/default-image.png").build();
    }
}
