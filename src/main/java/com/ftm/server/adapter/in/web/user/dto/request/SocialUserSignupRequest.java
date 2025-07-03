package com.ftm.server.adapter.in.web.user.dto.request;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class SocialUserSignupRequest {
    @NotNull private final AgeGroup age;

    private final List<HashtagCategory> hashtags;
}
