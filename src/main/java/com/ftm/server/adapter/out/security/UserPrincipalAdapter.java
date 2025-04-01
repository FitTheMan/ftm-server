package com.ftm.server.adapter.out.security;

import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** 시큐리티에서 유저 인증을 수행하고 컨텍스트에 저장할 인증 객체를 생성하는 서비스 */
@Adapter
@RequiredArgsConstructor
public class UserPrincipalAdapter implements UserDetailsService {

    private final LoadUserForAuthPort loadUserForAuthPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User saved =
                loadUserForAuthPort
                        .loadUserByEmail(FindByEmailQuery.of(email))
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 탈퇴한 회원인 경우 인증 실패 예외 처리
        if (saved.getIsDeleted() && saved.getDeletedAt() != null) {
            throw new CustomException(ErrorResponseCode.INVALID_CREDENTIALS);
        }

        return UserPrincipal.of(saved);
    }
}
