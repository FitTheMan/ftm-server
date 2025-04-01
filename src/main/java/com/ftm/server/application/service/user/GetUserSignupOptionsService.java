package com.ftm.server.application.service.user;

import com.ftm.server.adapter.in.web.user.dto.response.UserSignupOptionsResponse;
import com.ftm.server.application.port.in.user.GetUserSignupOptionsUseCase;
import com.ftm.server.application.vo.user.UserSignupOptionsVo;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserSignupOptionsService implements GetUserSignupOptionsUseCase {

    @Override
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
