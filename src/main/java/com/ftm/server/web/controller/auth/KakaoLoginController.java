package com.ftm.server.web.controller.auth;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.dto.command.KakaoAuthCommand;
import com.ftm.server.application.usecase.auth.KakaoLoginUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.enums.SocialProvider;
import com.ftm.server.domain.vo.PendingSocialUserVo;
import com.ftm.server.domain.vo.SocialLoginOutcomeVo;
import com.ftm.server.domain.vo.SocialLoginSuccessVo;
import com.ftm.server.infrastructure.security.AuthenticationService;
import com.ftm.server.web.dto.request.KakaoLoginRequest;
import com.ftm.server.web.dto.response.SocialLoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final AuthenticationService authenticationService;

    @PostMapping("/api/auth/login/kakao")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> kakaoLogin(
            @RequestBody KakaoLoginRequest request,
            HttpServletRequest req,
            HttpServletResponse res) {
        SocialLoginOutcomeVo result = kakaoLoginUseCase.kakaoLogin(KakaoAuthCommand.from(request));

        // 가입된 유저인 경우 로그인 처리
        if (result.isRegistered()) {
            SocialLoginSuccessVo loginUser = (SocialLoginSuccessVo) result;

            Authentication auth =
                    authenticationService.createAuthenticationFromSocial(loginUser.getUser());
            authenticationService.saveAuthenticatedSession(auth, req, res);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.success(
                                    SuccessResponseCode.OK,
                                    SocialLoginResponse.from(
                                            SocialProvider.KAKAO, loginUser.getUserSummaryVo())));
        }

        // 가입이 필요한 유저인 경우
        PendingSocialUserVo pendingSocialUser = (PendingSocialUserVo) result;

        // 임시 세션 등록
        HttpSession session = req.getSession(true);
        session.setAttribute(PENDING_SOCIAL_USER_SESSION_KEY, pendingSocialUser);
        session.setMaxInactiveInterval(PENDING_SOCIAL_USER_SESSION_TTL);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(SuccessResponseCode.ACCEPTED));
    }
}
