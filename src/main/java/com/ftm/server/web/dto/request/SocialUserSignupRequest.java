package com.ftm.server.web.dto.request;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class SocialUserSignupRequest {
    @NotNull private final AgeGroup age;

    private final List<HashTag> hashtags;
}
