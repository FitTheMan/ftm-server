package com.ftm.server.infrastructure.security;

import com.ftm.server.adapter.gateway.repository.UserRepository;
import com.ftm.server.common.annotation.InfraService;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.entity.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** 시큐리티에서 유저 인증을 수행하고 컨텍스트에 저장할 인증 객체를 생성하는 서비스 */
@InfraService
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User saved =
                userRepository.findByEmail(email).orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 탈퇴한 회원인 경우 인증 실패 예외 처리
        if (saved.getIsDeleted() && saved.getDeletedAt() != null) {
            throw new CustomException(ErrorResponseCode.INVALID_CREDENTIALS);
        }

        return UserPrincipal.of(saved);
    }
}
