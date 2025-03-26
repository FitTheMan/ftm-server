package com.ftm.server.application.service;

import com.ftm.server.application.dto.command.EmailVerificationLogCreationCommand;
import com.ftm.server.application.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.application.dto.query.FindByEmailQuery;
import com.ftm.server.application.dto.query.FindEmailVerificationLogsByEmailQuery;
import com.ftm.server.application.port.repository.EmailVerificationLogsRepository;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationLogsService {
    private final EmailVerificationLogsRepository emailVerificationLogsRepository;

    public Optional<EmailVerificationLogs> findEmailVerificationLogsByEmail(
            FindByEmailQuery query) {
        return emailVerificationLogsRepository.findByEmail(query.getEmail());
    }

    public void saveEmailVerificationLogs(EmailVerificationLogCreationCommand command) {
        emailVerificationLogsRepository.save(EmailVerificationLogs.from(command));
    }

    public Optional<EmailVerificationLogs> findByAuthenticationCode(
            EmailCodeVerificationQuery query) {
        return emailVerificationLogsRepository.findByVerificationCodeAndEmail(
                query.getCode(), query.getEmail());
    }

    public Optional<EmailVerificationLogs> findVerifiedOneByEmail(
            FindEmailVerificationLogsByEmailQuery query) {
        return emailVerificationLogsRepository.findByEmailAndIsVerified(query.getEmail(), true);
    }
}
