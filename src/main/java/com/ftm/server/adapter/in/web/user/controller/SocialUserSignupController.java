package com.ftm.server.adapter.in.web.user.controller;

import static com.ftm.server.common.consts.StaticConsts.PENDING_SOCIAL_USER_SESSION_KEY;

import com.ftm.server.adapter.in.web.user.dto.request.SocialUserSignupRequest;
import com.ftm.server.adapter.in.web.user.dto.response.SocialUserSignupResponse;
import com.ftm.server.application.command.user.SocialUserSignupCommand;
import com.ftm.server.application.port.in.user.SocialUserSignupUseCase;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.vo.auth.PendingSocialUserVo;
import com.ftm.server.application.vo.user.SocialUserSignupSummaryVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.domain.entity.User;
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
public class SocialUserSignupController {

    private final SocialUserSignupUseCase socialUserSignupUseCase;

    private final SecurityAuthenticationPort securityAuthenticationPort;

    @PostMapping("api/users/social")
    public ResponseEntity<ApiResponse<SocialUserSignupResponse>> socialUserSignup(
            @RequestBody @Valid SocialUserSignupRequest request,
            HttpServletRequest req,
            HttpServletResponse res) {

        HttpSession session = req.getSession(false); // session 조회

        if (session == null) { // 잘못된 session 값일 경우 예외 반환
            throw new CustomException(ErrorResponseCode.INVALID_SEESION_FOR_SOCIAL_USER_SIGNUP);
        }

        // 세션에서 저장된값 가져오기
        PendingSocialUserVo pendingSocialUser =
                (PendingSocialUserVo) session.getAttribute(PENDING_SOCIAL_USER_SESSION_KEY);

        SocialUserSignupCommand command = SocialUserSignupCommand.from(request, pendingSocialUser);

        SocialUserSignupSummaryVo summary = socialUserSignupUseCase.execute(command);

        User user = summary.getUser();

        session.invalidate();

        // 인증 정보 저장
        Authentication auth = securityAuthenticationPort.createAuthenticationFromSocial(user);
        securityAuthenticationPort.saveAuthenticatedSession(auth, req, res);

        SocialUserSignupResponse response = SocialUserSignupResponse.from(summary);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED, response));
    }
}
