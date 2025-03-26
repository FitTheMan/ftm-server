package com.ftm.server.application.dto.command;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import com.ftm.server.web.dto.request.GeneralUserSignupRequest;
import java.util.List;
import lombok.Data;

@Data
public class GeneralUserSignupCommand {

    private final String email;
    private final String password;
    private final AgeGroup age;
    private final List<HashTag> hashtags;

    public static GeneralUserSignupCommand from(GeneralUserSignupRequest request) {
        return new GeneralUserSignupCommand(
                request.getEmail(), request.getPassword(), request.getAge(), request.getHashtags());
    }
}
