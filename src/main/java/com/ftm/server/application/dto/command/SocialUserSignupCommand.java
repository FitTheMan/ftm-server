package com.ftm.server.application.dto.command;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import com.ftm.server.domain.enums.SocialProvider;
import com.ftm.server.domain.vo.PendingSocialUserVo;
import com.ftm.server.web.dto.request.SocialUserSignupRequest;
import java.util.List;
import lombok.Data;

@Data
public class SocialUserSignupCommand {
    private final SocialProvider socialProvider;
    private final String socialId;
    private final AgeGroup age;
    private final List<HashTag> hashtags;

    public static SocialUserSignupCommand from(
            SocialUserSignupRequest request, PendingSocialUserVo vo) {
        return new SocialUserSignupCommand(
                vo.getSocialProvider(), vo.getSocialId(), request.getAge(), request.getHashtags());
    }
}
