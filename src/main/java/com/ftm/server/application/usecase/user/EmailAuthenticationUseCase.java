package com.ftm.server.application.usecase.user;

import com.ftm.server.application.dto.command.EmailAuthenticationCommand;
import com.ftm.server.application.dto.command.EmailVerificationLogCreationCommand;
import com.ftm.server.application.dto.query.FindByEmailQuery;
import com.ftm.server.application.port.MailSenderPort;
import com.ftm.server.application.service.EmailVerificationLogsService;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.utils.RandomCodeCreator;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class EmailAuthenticationUseCase {

    private final MailSenderPort mailSenderPort;
    private final EmailVerificationLogsService emailVerificationLogsService;

    @Transactional
    public void sendEmailAuthenticationCode(EmailAuthenticationCommand command) {

        String authCode = RandomCodeCreator.generateAuthCode();
        String email = command.getEmail();

        Optional<EmailVerificationLogs> emailVerificationLogs =
                emailVerificationLogsService.findEmailVerificationLogsByEmail(
                        FindByEmailQuery.of(email));

        int MAX_TRIALS = 5;
        int BLOCK_MINUTES = 15;

        if (emailVerificationLogs.isEmpty()) { // 이메일 인증을 한번도 시도하지 않은 경우 log 새로 생성
            emailVerificationLogsService.saveEmailVerificationLogs(
                    EmailVerificationLogCreationCommand.of(email, authCode));
        } else if (emailVerificationLogs.get().getTrialNum()
                < MAX_TRIALS) { // 이메일 인증 시도를 한적 있으나 시도 횟수를 초과하지 않은 경우 log update
            emailVerificationLogs.get().updateVerificationStatus(authCode);
        } else if (emailVerificationLogs // 이메일 인증 시도 횟수를 초과했으나 마지막 시도 횟수로부터 15분 이상이 지난 경우 log 초기화
                .get()
                .getTokenIssuanceTime()
                .isBefore(LocalDateTime.now().minusMinutes(BLOCK_MINUTES))) {
            emailVerificationLogs.get().initializeVerificationStatus(authCode);
        } else { // 이메일 인증 시도 횟수를 단순히 초과한 경우
            throw new CustomException(ErrorResponseCode.EXCEED_NUMBER_OF_TRIAL);
        }
        mailSenderPort.sendEmail(email, authCode);
    }
}
