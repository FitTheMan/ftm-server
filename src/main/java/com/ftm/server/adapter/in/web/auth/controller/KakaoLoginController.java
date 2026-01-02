package com.ftm.server.adapter.in.web.auth.controller;

import static com.ftm.server.common.consts.StaticConsts.PENDING_SOCIAL_USER_SESSION_KEY;
import static com.ftm.server.common.consts.StaticConsts.PENDING_SOCIAL_USER_SESSION_TTL;

import com.ftm.server.adapter.in.web.auth.dto.request.KakaoLoginRequest;
import com.ftm.server.adapter.in.web.auth.dto.response.SocialLoginResponse;
import com.ftm.server.application.command.auth.KakaoLoginCommand;
import com.ftm.server.application.port.in.auth.KakaoLoginUseCase;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.vo.auth.PendingSocialUserVo;
import com.ftm.server.application.vo.auth.SocialLoginOutcomeVo;
import com.ftm.server.application.vo.auth.SocialLoginSuccessVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.enums.SocialProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    private final SecurityAuthenticationPort securityAuthenticationPort;

    @PostMapping("/api/auth/login/kakao")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> kakaoLogin(
            @RequestBody @Valid KakaoLoginRequest request,
            HttpServletRequest req,
            HttpServletResponse res) {
        SocialLoginOutcomeVo result = kakaoLoginUseCase.execute(KakaoLoginCommand.from(request));

        // 가입된 유저인 경우 로그인 처리
        if (result.isRegistered()) {
            SocialLoginSuccessVo loginUser = (SocialLoginSuccessVo) result;

            Authentication auth =
                    securityAuthenticationPort.createAuthenticationFromSocial(loginUser.getUser());
            securityAuthenticationPort.saveAuthenticatedSession(auth, req, res);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.success(
                                    SuccessResponseCode.OK,
                                    SocialLoginResponse.from(
                                            SocialProvider.KAKAO,
                                            loginUser.getAuthenticatedUserVo())));
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
