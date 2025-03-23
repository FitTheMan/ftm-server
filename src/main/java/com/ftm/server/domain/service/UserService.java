package com.ftm.server.domain.service;

import com.ftm.server.adapter.gateway.repository.UserRepository;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.domain.dto.vo.EmailDuplicationVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public EmailDuplicationVo isEmailDuplicated(FindByEmailQuery query) {
        return EmailDuplicationVo.of(userRepository.existsByEmail(query.getEmail()));
    }
}
