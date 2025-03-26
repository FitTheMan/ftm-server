package com.ftm.server.application.usecase.user;

import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import com.ftm.server.domain.vo.UserSignupOptionsVo;
import com.ftm.server.web.dto.response.UserSignupOptionsResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetUserSignupOptionsUseCase {

    public UserSignupOptionsVo execute() {
        List<UserSignupOptionsResponse.EnumDescriptors> ages =
                Arrays.stream(AgeGroup.values())
                        .map(UserSignupOptionsResponse.EnumDescriptors::from)
                        .toList();

        List<UserSignupOptionsResponse.EnumDescriptors> hashtags =
                Arrays.stream(HashTag.values())
                        .map(UserSignupOptionsResponse.EnumDescriptors::from)
                        .toList();
        return UserSignupOptionsVo.of(ages, hashtags);
    }
}
