package com.ftm.server.application.service.auth;

import com.ftm.server.application.command.auth.KakaoLoginCommand;
import com.ftm.server.application.port.in.auth.KakaoLoginUseCase;
import com.ftm.server.application.port.out.oauth.SocialOAuthClientPort;
import com.ftm.server.application.port.out.persistence.auth.LoadGroomingLevelForAuthPort;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.auth.LoadUserImageForAuthPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindBySocialValueQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import com.ftm.server.application.vo.auth.PendingSocialUserVo;
import com.ftm.server.application.vo.auth.SocialLoginOutcomeVo;
import com.ftm.server.application.vo.auth.SocialLoginSuccessVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.infrastructure.oauth.kakao.KakaoAuthUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements KakaoLoginUseCase {

    private final SocialOAuthClientPort<KakaoLoginCommand, KakaoAuthUser> kakaoOAuthClientPort;
    private final LoadUserForAuthPort loadUserForAuthPort;
    private final LoadUserImageForAuthPort loadUserImageForAuthPort;
    private final LoadGroomingLevelForAuthPort loadGroomingLevelForAuthPort;

    @Override
    public SocialLoginOutcomeVo execute(KakaoLoginCommand command) {

        // 카카오 로그인 인증 API
        KakaoAuthUser kakaoUser = kakaoOAuthClientPort.authenticate(command);

        Optional<User> saved =
                loadUserForAuthPort.loadUserBySocialProviderAndSocialId(
                        FindBySocialValueQuery.of(
                                kakaoUser.getSocialProvider(), kakaoUser.getSocialId()));

        // 가입된 회원인 경우
        if (saved.isPresent()) {
            User user = saved.get();
            UserImage image =
                    loadUserImageForAuthPort
                            .loadUserImageByUserId(FindByUserIdQuery.of(user.getId()))
                            .orElseThrow(
                                    () ->
                                            new CustomException(
                                                    ErrorResponseCode.USER_IMAGE_NOT_FOUND));
            GroomingLevel groomingLevel = null;
            if (user.getGroomingLevelId() != null) {
                groomingLevel =
                        loadGroomingLevelForAuthPort
                                .loadGroomingLevelById(FindByIdQuery.of(user.getGroomingLevelId()))
                                .orElseThrow(
                                        () ->
                                                new CustomException(
                                                        ErrorResponseCode
                                                                .GROOMING_LEVEL_NOT_FOUND));
            }

            return SocialLoginSuccessVo.from(
                    user, AuthenticatedUserVo.of(user, image, groomingLevel));
        }

        // 가입된 회원이 아닌 경우
        return PendingSocialUserVo.from(kakaoUser.getSocialProvider(), kakaoUser.getSocialId());
    }
}
