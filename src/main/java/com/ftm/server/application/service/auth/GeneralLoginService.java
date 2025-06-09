package com.ftm.server.application.service.auth;

import com.ftm.server.application.command.auth.GeneralLoginCommand;
import com.ftm.server.application.port.in.auth.GeneralLoginUseCase;
import com.ftm.server.application.port.out.persistence.auth.LoadGroomingLevelForAuthPort;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.auth.LoadUserImageForAuthPort;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.auth.AuthenticatedUserVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralLoginService implements GeneralLoginUseCase {

    private final SecurityAuthenticationPort securityAuthenticationPort;
    private final LoadUserForAuthPort loadUserForAuthPort;
    private final LoadUserImageForAuthPort loadUserImageForAuthPort;
    private final LoadGroomingLevelForAuthPort loadGroomingLevelForAuthPort;

    @Override
    @Transactional
    public AuthenticatedUserVo execute(
            GeneralLoginCommand command, HttpServletRequest req, HttpServletResponse res) {

        // 인증을 수행하고 인증 객체 생성 (실패 시 예외 발생)
        Authentication auth = createAuthenticationOrThrow(command);
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        User user =
                loadUserForAuthPort
                        .loadUserById(FindByIdQuery.of(userPrincipal.getId()))
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        UserImage userImage =
                loadUserImageForAuthPort
                        .loadUserImageByUserId(FindByUserIdQuery.of(user.getId()))
                        .orElseThrow(
                                () -> new CustomException(ErrorResponseCode.USER_IMAGE_NOT_FOUND));

        GroomingLevel groomingLevel = null;
        if (user.getGroomingLevelId() != null) {
            groomingLevel =
                    loadGroomingLevelForAuthPort
                            .loadGroomingLevelById(FindByIdQuery.of(user.getGroomingLevelId()))
                            .orElseThrow(
                                    () ->
                                            new CustomException(
                                                    ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND));
        }

        // 인증 세션 등록 (시큐리티 컨텍스트 등록)
        securityAuthenticationPort.saveAuthenticatedSession(auth, req, res);

        return AuthenticatedUserVo.of(user, userImage, groomingLevel);
    }

    private Authentication createAuthenticationOrThrow(GeneralLoginCommand command) {
        try {
            return securityAuthenticationPort.createAuthenticationFromCredentials(command);
        } catch (AuthenticationException ex) {
            throw new CustomException(ErrorResponseCode.INVALID_CREDENTIALS);
        }
    }
}
