package com.ftm.server.domain.dto.vo;

import com.ftm.server.adapter.dto.response.UserSignupOptionsResponse;
import java.util.List;
import lombok.Data;

@Data
public class UserSignupOptionsVo {

    private final List<UserSignupOptionsResponse.EnumDescriptors> ages;
    private final List<UserSignupOptionsResponse.EnumDescriptors> hashtags;

    public static UserSignupOptionsVo of(
            List<UserSignupOptionsResponse.EnumDescriptors> ages,
            List<UserSignupOptionsResponse.EnumDescriptors> hashtags) {
        return new UserSignupOptionsVo(ages, hashtags);
    }
}
