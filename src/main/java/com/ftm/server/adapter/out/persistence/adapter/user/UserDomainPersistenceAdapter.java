package com.ftm.server.adapter.out.persistence.adapter.user;

import com.ftm.server.adapter.out.persistence.mapper.EmailVerificationLogsMapper;
import com.ftm.server.adapter.out.persistence.mapper.UserImageMapper;
import com.ftm.server.adapter.out.persistence.mapper.UserMapper;
import com.ftm.server.adapter.out.persistence.model.EmailVerificationLogsJpaEntity;
import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.adapter.out.persistence.repository.EmailVerificationLogsRepository;
import com.ftm.server.adapter.out.persistence.repository.GroomingLevelRepository;
import com.ftm.server.adapter.out.persistence.repository.UserImageRepository;
import com.ftm.server.adapter.out.persistence.repository.UserRepository;
import com.ftm.server.application.command.user.EmailVerificationLogCreationCommand;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.query.*;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class UserDomainPersistenceAdapter
        implements LoadEmailVerificationLogPort,
                SaveEmailVerificationLogPort,
                UpdateEmailVerificationLogPort,
                CheckUserPort,
                SaveUserPort,
                SaveUserImagePort,
                LoadUserPort,
                LoadUserImagePort,
                UpdateUserInfoPort,
                UpdateUserImagePort {

    // repository
    private final EmailVerificationLogsRepository emailVerificationLogsRepository;
    private final UserRepository userRepository;
    private final GroomingLevelRepository groomingLevelRepository;
    private final UserImageRepository userImageRepository;

    // mapper
    private final EmailVerificationLogsMapper emailVerificationLogsMapper;
    private final UserMapper userMapper;
    private final UserImageMapper userImageMapper;

    @Override
    public Optional<EmailVerificationLogs> loadEmailVerificationLogByEmail(FindByEmailQuery query) {
        Optional<EmailVerificationLogsJpaEntity> emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findByEmail(query.getEmail());
        return emailVerificationLogsJpaEntity.map(emailVerificationLogsMapper::toDomainEntity);
    }

    @Override
    public Optional<EmailVerificationLogs> loadEmailVerificationLogByEmailAndCode(
            EmailCodeVerificationQuery query) {
        Optional<EmailVerificationLogsJpaEntity> emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findByVerificationCodeAndEmail(
                        query.getCode(), query.getEmail());
        return emailVerificationLogsJpaEntity.map(emailVerificationLogsMapper::toDomainEntity);
    }

    @Override
    public void saveEmailVerificationLogs(EmailVerificationLogCreationCommand command) {
        emailVerificationLogsRepository.save(
                emailVerificationLogsMapper.toJpaEntity(EmailVerificationLogs.from(command)));
    }

    @Override
    public void updateEmailVerificationLog(EmailVerificationLogs emailVerificationLogs) {
        EmailVerificationLogsJpaEntity emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findById(emailVerificationLogs.getId()).get();
        emailVerificationLogsJpaEntity.updateFromDomainEntity(emailVerificationLogs);
    }

    @Override
    public Boolean checksUserByEmail(FindByEmailQuery query) {
        return userRepository.existsByEmail(query.getEmail());
    }

    @Override
    public User saveUser(User user) {
        UserJpaEntity userJpaEntity = userMapper.toJpaEntity(user, null);
        UserJpaEntity savedUser = userRepository.save(userJpaEntity);
        return userMapper.toDomainEntity(savedUser);
    }

    @Override
    public void saveUserDefaultImage(UserImage userImage) {
        UserJpaEntity userJpaEntity = userRepository.findById(userImage.getUserId()).get();
        UserImageJpaEntity userImageJpaEntity =
                userImageMapper.toJpaEntity(userImage, userJpaEntity);
        userImageRepository.save(userImageJpaEntity);
    }

    @Override
    public Boolean checksUserBySocialValue(FindBySocialValueQuery query) {
        return userRepository.existsBySocialIdAndSocialProvider(
                query.getSocialId(), query.getSocialProvider());
    }

    @Override
    public User saveSocialUser(User user) {
        UserJpaEntity userJpaEntity = userMapper.toJpaEntity(user, null);
        UserJpaEntity savedUser = userRepository.save(userJpaEntity);
        return userMapper.toDomainEntity(savedUser);
    }

    @Override
    public User loadUserById(FindByUserIdQuery query) {
        UserJpaEntity userJpaEntity =
                userRepository
                        .findById(query.getUserId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);
        return userMapper.toDomainEntity(userJpaEntity);
    }

    @Override
    public UserImage loadUserImageByUserId(FindByUserIdQuery query) {
        UserImageJpaEntity userImageJpaEntity =
                userImageRepository.findByUserId(query.getUserId()).orElse(null);
        if (userImageJpaEntity != null) {
            return userImageMapper.toDomainEntity(userImageJpaEntity);
        }
        return null;
    }

    @Override
    public void updateUserInfo(User user) {
        UserJpaEntity savedUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);
        GroomingLevelJpaEntity groomingLevelJpaEntity =
                user.getGroomingLevelId() == null
                        ? null
                        : groomingLevelRepository.findById(user.getGroomingLevelId()).orElse(null);

        savedUser.updateFromDomainEntity(user, groomingLevelJpaEntity);

        userRepository.save(savedUser);
    }

    @Override
    public void updateUserImage(UserImage userImage) {
        UserImageJpaEntity userImageJpaEntity =
                userImageRepository.findByUserId(userImage.getUserId()).orElse(null);
        if (userImageJpaEntity == null) {
            log.error("[USER_IMAGE_NOT_FOUND] : 사용자의 이미지 data를 찾을 수 없음.");
        }

        userImageJpaEntity.updateFromDomainEntity(userImage);
    }
}
