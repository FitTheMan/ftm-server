package com.ftm.server.application.dto.command;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import lombok.Data;

@Data
public class GeneralUserCreationCommand {
    private final String email;
    private final String password;
    private final String nickName;
    private final AgeGroup ageGroup;
    private final HashTag[] hashTags;

    public static GeneralUserCreationCommand of(
            String email, String password, String nickName, AgeGroup ageGroup, HashTag[] hashTag) {
        return new GeneralUserCreationCommand(email, password, nickName, ageGroup, hashTag);
    }
}
