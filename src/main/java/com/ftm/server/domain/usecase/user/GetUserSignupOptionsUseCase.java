package com.ftm.server.domain.usecase.user;

import com.ftm.server.adapter.dto.response.UserSignupOptionsResponse;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.dto.vo.UserSignupOptionsVo;
import com.ftm.server.entity.enums.AgeGroup;
import com.ftm.server.entity.enums.HashTag;
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
