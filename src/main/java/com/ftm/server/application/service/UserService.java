package com.ftm.server.application.service;

import com.ftm.server.application.dto.command.GeneralUserCreationCommand;
import com.ftm.server.application.dto.query.FindByEmailQuery;
import com.ftm.server.application.dto.query.FindByIdQuery;
import com.ftm.server.application.dto.query.FindSocialUserQuery;
import com.ftm.server.application.port.repository.UserRepository;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.vo.EmailDuplicationVo;
import java.util.Optional;
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

    public User queryUser(FindByIdQuery query) {
        return userRepository
                .findById(query.getId())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);
    }

    public Optional<User> querySocialUser(FindSocialUserQuery query) {
        return userRepository.findBySocialProviderAndSocialId(
                query.getSocialProvider(), query.getSocialId());
    }

    public User createGeneralUser(GeneralUserCreationCommand command) {
        User user = User.createGeneralUser(command);
        userRepository.save(user);
        return user;
    }

    public Boolean userCheckByEmail(FindByEmailQuery query) {
        return userRepository.existsByEmail(query.getEmail());
    }
}
