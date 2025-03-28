package com.ftm.server.application.usecase.auth;

import com.ftm.server.application.dto.command.KakaoAuthCommand;
import com.ftm.server.application.dto.query.FindByUserIdQuery;
import com.ftm.server.application.dto.query.FindSocialUserQuery;
import com.ftm.server.application.port.SocialAuthClientPort;
import com.ftm.server.application.service.UserImageService;
import com.ftm.server.application.service.UserService;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.domain.vo.PendingSocialUserVo;
import com.ftm.server.domain.vo.SocialLoginOutcomeVo;
import com.ftm.server.domain.vo.SocialLoginSuccessVo;
import com.ftm.server.domain.vo.UserSummaryVo;
import com.ftm.server.infrastructure.oauth.kakao.KakaoAuthUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class KakaoLoginUseCase {

    private final SocialAuthClientPort<KakaoAuthCommand, KakaoAuthUser> oAuthClientPort;
    private final UserService userService;
    private final UserImageService userImageService;

    public SocialLoginOutcomeVo kakaoLogin(KakaoAuthCommand command) {
        // 카카오 인증 수행
        KakaoAuthUser kakaoUser = oAuthClientPort.authenticate(command);

        Optional<User> saved =
                userService.querySocialUser(
                        FindSocialUserQuery.of(
                                kakaoUser.getSocialProvider(), kakaoUser.getSocialId()));

        // 가입된 회원인 경우
        if (saved.isPresent()) {
            User user = saved.get();
            UserImage image =
                    userImageService.queryUserImageByUserId(FindByUserIdQuery.of(user.getId()));

            return SocialLoginSuccessVo.from(user, UserSummaryVo.of(user, image));
        }

        // 가입된 회원이 아닌 경우
        return PendingSocialUserVo.from(kakaoUser.getSocialProvider(), kakaoUser.getSocialId());
    }
}
