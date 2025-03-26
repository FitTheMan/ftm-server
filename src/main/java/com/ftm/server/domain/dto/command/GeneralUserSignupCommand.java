package com.ftm.server.domain.dto.command;

import com.ftm.server.adapter.dto.request.GeneralUserSignupRequest;
import com.ftm.server.entity.enums.AgeGroup;
import com.ftm.server.entity.enums.HashTag;
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
