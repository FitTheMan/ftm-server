package com.ftm.server.application.command.user;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import com.ftm.server.domain.enums.SocialProvider;
import java.util.List;
import lombok.Data;

@Data
public class SocialUserCreationCommand {
    private final SocialProvider provider;
    private final String socialId;
    private final String nickname;
    private final AgeGroup age;
    private final List<HashtagCategory> hashtags;

    public static SocialUserCreationCommand of(
            SocialProvider provider,
            String socialId,
            String nickname,
            AgeGroup age,
            List<HashtagCategory> hashtags) {
        return new SocialUserCreationCommand(provider, socialId, nickname, age, hashtags);
    }
}
