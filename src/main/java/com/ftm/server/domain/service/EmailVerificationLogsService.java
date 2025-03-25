package com.ftm.server.domain.service;

import com.ftm.server.adapter.gateway.repository.EmailVerificationLogsRepository;
import com.ftm.server.domain.dto.command.EmailVerificationLogCreationCommand;
import com.ftm.server.domain.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.entity.entities.EmailVerificationLogs;
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
}
