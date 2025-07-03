package com.ftm.server.application.command.user;

import com.ftm.server.adapter.in.web.user.dto.request.SocialUserSignupRequest;
import com.ftm.server.application.vo.auth.PendingSocialUserVo;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import com.ftm.server.domain.enums.SocialProvider;
import java.util.List;
import lombok.Data;

@Data
public class SocialUserSignupCommand {
    private final SocialProvider socialProvider;
    private final String socialId;
    private final AgeGroup age;
    private final List<HashtagCategory> hashtags;

    public static SocialUserSignupCommand from(
            SocialUserSignupRequest request, PendingSocialUserVo vo) {
        return new SocialUserSignupCommand(
                vo.getSocialProvider(), vo.getSocialId(), request.getAge(), request.getHashtags());
    }
}
