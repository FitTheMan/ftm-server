package com.ftm.server.application.service.user;

import com.ftm.server.adapter.in.web.user.dto.response.GeneralUserSignupResponse;
import com.ftm.server.application.command.user.DeleteByEmailCommand;
import com.ftm.server.application.command.user.DeleteUserByEmailCommand;
import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.command.user.GeneralUserSignupCommand;
import com.ftm.server.application.port.in.user.GeneralUserSignupUseCase;
import com.ftm.server.application.port.in.user.UserHardDeleteByEmailUseCase;
import com.ftm.server.application.port.out.persistence.user.CheckUserPort;
import com.ftm.server.application.port.out.persistence.user.DeleteEmailVerificationLogPort;
import com.ftm.server.application.port.out.persistence.user.LoadEmailVerificationLogPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.utils.RandomNickNameCreator;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralUserSignupService implements GeneralUserSignupUseCase {

    // usecase
    private final UserHardDeleteByEmailUseCase userHardDeleteByEmailUseCase;

    // service
    private final LoadEmailVerificationLogPort loadEmailVerificationLogPort;
    private final DeleteEmailVerificationLogPort deleteEmailVerificationLogPort;
    private final CheckUserPort checksUserPort;
    private final SaveUserPort saveUserPort;
    private final SaveUserImagePort saveUserImagePort;

    // gateway
    private final SecurityAuthenticationPort authenticationPort;

    @Transactional
    @Override
    public GeneralUserSignupResponse execute(GeneralUserSignupCommand command) {
        String email = command.getEmail();
        Optional<EmailVerificationLogs> emailVerificationLogs =
                loadEmailVerificationLogPort.loadEmailVerificationLogByEmail(
                        FindByEmailQuery.of(email));

        if (checksUserPort.checksNotDeletedUserByEmail(
                FindByEmailQuery.of(email))) { // 기존에 가입된 회원인지 검사(삭제되지 않은 user 에 한해서)
            throw new CustomException(ErrorResponseCode.USER_ALREADY_EXISTS);
        }

        if (emailVerificationLogs.isEmpty()) { // 이메일 인증이 완료되지 않음.
            throw new CustomException(ErrorResponseCode.EMAIL_NOT_VERIFIED);
        }

        // 회원 탈퇴 후 복구 없이 재가입하는 경우, 기존의 계정 정보 즉시 삭제
        if (checksUserPort.checksUserSoftDeletedByEmail(FindByEmailQuery.of(email))) {
            userHardDeleteByEmailUseCase.execute(DeleteUserByEmailCommand.of(email));
        }

        String nickname = RandomNickNameCreator.generateNickname(); // random 닉네임 생성

        GeneralUserCreationCommand convertedCommand =
                GeneralUserCreationCommand.of(
                        command.getEmail(),
                        authenticationPort.passwordEncode(command.getPassword()),
                        nickname,
                        command.getAge(),
                        command.getHashtags());

        User user = saveUserPort.saveUser(User.createGeneralUser(convertedCommand));
        saveUserImagePort.saveUserDefaultImage(UserImage.createUserImage(user.getId()));

        // 회원가입 완료 후 해당 이메일의 인증 로그 삭제
        deleteEmailVerificationLogPort.deleteEmailVerificationLogsByEmail(
                DeleteByEmailCommand.of(email));

        return GeneralUserSignupResponse.of(user.getId());
    }
}
