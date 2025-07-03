package com.ftm.server.adapter.in.web.user.dto.request;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private final String nickname;
    private final AgeGroup age;
    private final HashtagCategory[] hashtags;
    private final String imageAction;
}
