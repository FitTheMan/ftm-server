package com.ftm.server.adapter.out.persistence.adapter.auth;

import com.ftm.server.adapter.out.persistence.mapper.UserImageMapper;
import com.ftm.server.adapter.out.persistence.mapper.UserMapper;
import com.ftm.server.adapter.out.persistence.repository.UserImageRepository;
import com.ftm.server.adapter.out.persistence.repository.UserRepository;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.auth.LoadUserImageForAuthPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindBySocialValueQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class AuthDomainPersistenceAdapter implements LoadUserForAuthPort, LoadUserImageForAuthPort {

    // Repository
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    // Mapper
    private final UserMapper userMapper;
    private final UserImageMapper userImageMapper;

    @Override
    public Optional<User> loadUserById(FindByIdQuery query) {
        return userRepository.findById(query.getId()).map(userMapper::toDomainEntity);
    }

    @Override
    public Optional<User> loadUserByEmail(FindByEmailQuery query) {
        return userRepository.findByEmail(query.getEmail()).map(userMapper::toDomainEntity);
    }

    @Override
    public Optional<User> loadUserBySocialProviderAndSocialId(FindBySocialValueQuery query) {
        return userRepository
                .findBySocialProviderAndSocialId(query.getSocialProvider(), query.getSocialId())
                .map(userMapper::toDomainEntity);
    }

    @Override
    public Optional<UserImage> loadUserImageByUserId(FindByUserIdQuery query) {
        return userImageRepository
                .findByUserId(query.getUserId())
                .map(userImageMapper::toDomainEntity);
    }
}
