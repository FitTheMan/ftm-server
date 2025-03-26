package com.ftm.server.adapter.dto.response;

import com.ftm.server.domain.dto.vo.UserSignupOptionsVo;
import com.ftm.server.entity.enums.AgeGroup;
import com.ftm.server.entity.enums.HashTag;
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
        public static EnumDescriptors from(HashTag hashTag) {
            return new EnumDescriptors(hashTag.name(), hashTag.getValue());
        }

        public static EnumDescriptors from(AgeGroup ageGroup) {
            return new EnumDescriptors(ageGroup.name(), ageGroup.getValue());
        }
    }
}
