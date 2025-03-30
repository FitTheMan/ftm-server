package com.ftm.server.application.dto.command;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import java.util.List;
import lombok.Data;

@Data
public class GeneralUserCreationCommand {
    private final String email;
    private final String password;
    private final String nickName;
    private final AgeGroup ageGroup;
    private final List<HashTag> hashtags;

    public static GeneralUserCreationCommand of(
            String email,
            String password,
            String nickName,
            AgeGroup ageGroup,
            List<HashTag> hashtag) {
        return new GeneralUserCreationCommand(email, password, nickName, ageGroup, hashtag);
    }
}
