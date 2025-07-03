package com.ftm.server.application.command.user;

import com.ftm.server.adapter.in.web.user.dto.request.GeneralUserSignupRequest;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import java.util.List;
import lombok.Data;

@Data
public class GeneralUserSignupCommand {

    private final String email;
    private final String password;
    private final AgeGroup age;
    private final List<HashtagCategory> hashtags;

    public static GeneralUserSignupCommand from(GeneralUserSignupRequest request) {
        return new GeneralUserSignupCommand(
                request.getEmail(), request.getPassword(), request.getAge(), request.getHashtags());
    }
}
