package com.ftm.server.domain.service;

import com.ftm.server.adapter.gateway.repository.UserRepository;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.domain.dto.query.FindByIdQuery;
import com.ftm.server.domain.dto.vo.EmailDuplicationVo;
import com.ftm.server.entity.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public EmailDuplicationVo isEmailDuplicated(FindByEmailQuery query) {
        return EmailDuplicationVo.of(userRepository.existsByEmail(query.getEmail()));
    }

    public User queryUser(FindByIdQuery query) {
        return userRepository
                .findById(query.getId())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);
    }
}
