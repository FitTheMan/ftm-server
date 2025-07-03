package com.ftm.server.application.command.user;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import java.util.List;
import lombok.Data;

@Data
public class GeneralUserCreationCommand {
    private final String email;
    private final String password;
    private final String nickName;
    private final AgeGroup ageGroup;
    private final List<HashtagCategory> hashtags;

    public static GeneralUserCreationCommand of(
            String email,
            String password,
            String nickName,
            AgeGroup ageGroup,
            List<HashtagCategory> hashtags) {
        return new GeneralUserCreationCommand(email, password, nickName, ageGroup, hashtags);
    }
}
