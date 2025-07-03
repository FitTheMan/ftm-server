package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.user.UserSignupOptionsVo;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import java.util.List;
import lombok.Data;

@Data
public class UserSignupOptionsResponse {

    private final List<EnumDescriptors> ages;
    private final List<EnumDescriptors> hashtags;

    public static UserSignupOptionsResponse from(UserSignupOptionsVo vo) {
        return new UserSignupOptionsResponse(vo.getAges(), vo.getHashtags());
    }

    public record EnumDescriptors(String value, String description) {
        public static EnumDescriptors from(HashtagCategory hashtagCategory) {
            return new EnumDescriptors(hashtagCategory.name(), hashtagCategory.getLabel());
        }

        public static EnumDescriptors from(AgeGroup ageGroup) {
            return new EnumDescriptors(ageGroup.name(), ageGroup.getValue());
        }
    }
}
