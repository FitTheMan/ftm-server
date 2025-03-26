package com.ftm.server.domain.usecase.user;

import com.ftm.server.adapter.gateway.AuthenticationGateway;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.utils.RandomNickNameCreator;
import com.ftm.server.domain.dto.command.GeneralUserCreationCommand;
import com.ftm.server.domain.dto.command.GeneralUserSignupCommand;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.domain.dto.query.FindEmailVerificationLogsByEmailQuery;
import com.ftm.server.domain.service.EmailVerificationLogsService;
import com.ftm.server.domain.service.UserImageService;
import com.ftm.server.domain.service.UserService;
import com.ftm.server.entity.entities.EmailVerificationLogs;
import com.ftm.server.entity.entities.User;
import com.ftm.server.entity.enums.HashTag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GeneralUserSignupUseCase {

    // service
    private final EmailVerificationLogsService emailVerificationLogsService;
    private final UserService userService;
    private final UserImageService userImageService;

    // gateway
    private final AuthenticationGateway authenticationGateway;

    public void execute(GeneralUserSignupCommand command) {

        String email = command.getEmail();
        Optional<EmailVerificationLogs> emailVerificationLogs =
                emailVerificationLogsService.findVerifiedOneByEmail(
                        FindEmailVerificationLogsByEmailQuery.of(email));

        if (userService.userCheckByEmail(FindByEmailQuery.of(email))) { // 기존에 가입된 회원인지 검사
            throw new CustomException(ErrorResponseCode.USER_ALREADY_EXISTS);
        }

        if (emailVerificationLogs.isEmpty()) { // 이메일 인증이 완료되지 않음.
            throw new CustomException(ErrorResponseCode.EMAIL_NOT_VERIFIED);
        }

        String nickname = RandomNickNameCreator.generateNickname(); // random 닉네임 생성
        int size = command.getHashtags().size();

        GeneralUserCreationCommand convertedCommand =
                GeneralUserCreationCommand.of(
                        command.getEmail(),
                        authenticationGateway.passwordEncode(command.getPassword()),
                        nickname,
                        command.getAge(),
                        command.getHashtags().toArray(new HashTag[size]));

        User user = userService.createGeneralUser(convertedCommand);
        userImageService.saveUserDefaultImage(user);
    }
}
