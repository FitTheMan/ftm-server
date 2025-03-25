package com.ftm.server.domain.usecase.auth;

import com.ftm.server.adapter.gateway.AuthenticationGateway;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.dto.command.UserLoginCommand;
import com.ftm.server.domain.dto.query.FindByIdQuery;
import com.ftm.server.domain.dto.query.FindByUserIdQuery;
import com.ftm.server.domain.dto.vo.UserSummaryVo;
import com.ftm.server.domain.service.UserImageService;
import com.ftm.server.domain.service.UserService;
import com.ftm.server.entity.entities.User;
import com.ftm.server.entity.entities.UserImage;
import com.ftm.server.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UserLoginUseCase {

    private final AuthenticationGateway securityAuthenticateGateway;
    private final UserService userService;
    private final UserImageService userImageService;

    @Transactional
    public UserSummaryVo login(
            UserLoginCommand command, HttpServletRequest req, HttpServletResponse res) {

        // 인증을 수행하고 인증 객체 생성 (실패 시 예외 발생)
        Authentication auth = createAuthenticationOrThrow(command);
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        User user = userService.queryUser(FindByIdQuery.of(userPrincipal.getId()));
        UserImage userImage =
                userImageService.queryUserImageByUserId(FindByUserIdQuery.of(user.getId()));

        // 인증 세션 등록 (시큐리티 컨텍스트 등록)
        securityAuthenticateGateway.saveAuthenticatedSession(auth, req, res);

        return UserSummaryVo.of(user, userImage);
    }

    private Authentication createAuthenticationOrThrow(UserLoginCommand command) {
        try {
            return securityAuthenticateGateway.createAuthenticationFromCredentials(command);
        } catch (AuthenticationException ex) {
            throw new CustomException(ErrorResponseCode.INVALID_CREDENTIALS);
        }
    }
}
