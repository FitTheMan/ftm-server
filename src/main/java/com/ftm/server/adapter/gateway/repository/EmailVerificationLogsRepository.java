package com.ftm.server.adapter.gateway.repository;

import com.ftm.server.entity.entities.EmailVerificationLogs;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationLogsRepository
        extends JpaRepository<EmailVerificationLogs, Long> {

    Optional<EmailVerificationLogs> findByEmail(String email);

    Optional<EmailVerificationLogs> findByVerificationCodeAndEmail(
            String verificationCode, String email);
}
