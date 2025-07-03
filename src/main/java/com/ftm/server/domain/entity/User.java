package com.ftm.server.domain.entity;

import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.command.user.SocialUserCreationCommand;
import com.ftm.server.domain.enums.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private AgeGroup ageGroup;
    private SocialProvider socialProvider;
    private String socialId;
    private Integer groomingScore;
    private Long groomingLevelId;
    private UserRole role;
    private HashtagCategory[] favoriteHashtags;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private User(
            Long id,
            String email,
            String password,
            String nickname,
            AgeGroup ageGroup,
            SocialProvider socialProvider,
            String socialId,
            Integer groomingScore,
            Long groomingLevelId,
            UserRole role,
            HashtagCategory[] favoriteHashtags,
            Boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.groomingScore = groomingScore;
        this.groomingLevelId = groomingLevelId;
        this.role = role;
        this.favoriteHashtags = favoriteHashtags;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User of(
            Long id,
            String email,
            String password,
            String nickname,
            AgeGroup ageGroup,
            SocialProvider socialProvider,
            String socialId,
            Integer groomingScore,
            Long groomingLevelId,
            UserRole role,
            HashtagCategory[] favoriteHashtags,
            Boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .ageGroup(ageGroup)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .groomingScore(groomingScore)
                .groomingLevelId(groomingLevelId)
                .role(role)
                .favoriteHashtags(favoriteHashtags)
                .isDeleted(isDeleted)
                .deletedAt(deletedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static User createGeneralUser(GeneralUserCreationCommand command) {

        HashtagCategory[] hashtags = null;

        if (command.getHashtags() != null && command.getHashtags().isEmpty()) {
            hashtags = command.getHashtags().toArray(new HashtagCategory[0]);
        }
        return User.builder()
                .email(command.getEmail())
                .password(command.getPassword())
                .nickname(command.getNickName())
                .ageGroup(command.getAgeGroup())
                .favoriteHashtags(hashtags)
                .groomingScore(0)
                .isDeleted(false)
                .role(UserRole.USER)
                .build();
    }

    public static User createSocailUser(SocialUserCreationCommand command) {

        HashtagCategory[] hashtags = null;

        if (command.getHashtags() != null && command.getHashtags().isEmpty()) {
            hashtags = command.getHashtags().toArray(new HashtagCategory[0]);
        }

        return User.builder()
                .socialProvider(command.getProvider())
                .socialId(command.getSocialId())
                .nickname(command.getNickname())
                .ageGroup(command.getAge())
                .favoriteHashtags(hashtags)
                .groomingScore(0)
                .isDeleted(false)
                .role(UserRole.USER)
                .build();
    }

    public static User createTestKakaoUser() {
        return User.builder()
                .nickname("test")
                .socialProvider(SocialProvider.KAKAO)
                .socialId("test_kakao_id")
                .groomingScore(0)
                .isDeleted(false)
                .role(UserRole.USER)
                .build();
    }

    public static User createAdminUser(String email, String password, String nickname) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(UserRole.ADMIN)
                .build();
    }

    public void updateGroomingInfo(Integer groomingScore, Long groomingLevelId) {
        this.groomingScore = groomingScore;
        this.groomingLevelId = groomingLevelId;
    }

    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateHashtag(HashtagCategory[] hashtags) {
        this.favoriteHashtags = hashtags;
    }

    public void updateAge(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void updateIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
