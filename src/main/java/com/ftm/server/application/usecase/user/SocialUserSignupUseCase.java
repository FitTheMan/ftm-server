package com.ftm.server.application.usecase.user;

import com.ftm.server.application.dto.command.SocialUserCreationCommand;
import com.ftm.server.application.dto.command.SocialUserSignupCommand;
import com.ftm.server.application.dto.query.FindBySocialValueQuery;
import com.ftm.server.application.service.UserImageService;
import com.ftm.server.application.service.UserService;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.utils.RandomNickNameCreator;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.domain.vo.SocialUserSignupSummaryVo;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SocialUserSignupUseCase {

    private final UserService userService;
    private final UserImageService userImageService;

    public SocialUserSignupSummaryVo execute(SocialUserSignupCommand command) {

        // 이미 가입된 회원인지 한번 더 확인
        if (userService.userCheckBySocialValue(
                FindBySocialValueQuery.of(command.getSocialProvider(), command.getSocialId()))) {
            throw new CustomException(ErrorResponseCode.USER_ALREADY_EXISTS);
        }

        String nickname = RandomNickNameCreator.generateNickname();

        SocialUserCreationCommand command1 =
                SocialUserCreationCommand.of(
                        command.getSocialProvider(),
                        command.getSocialId(),
                        nickname,
                        command.getAge(),
                        command.getHashtags());

        User user = userService.createSocialUser(command1);

        UserImage userImage = userImageService.saveUserDefaultImage(user);

        return SocialUserSignupSummaryVo.of(user, userImage.getObjectKey());
    }
}
