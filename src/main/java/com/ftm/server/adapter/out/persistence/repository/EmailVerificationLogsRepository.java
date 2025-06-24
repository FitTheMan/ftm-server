package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.EmailVerificationLogsJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationLogsRepository
        extends JpaRepository<EmailVerificationLogsJpaEntity, Long> {

    Optional<EmailVerificationLogsJpaEntity> findByEmail(String email);

    Optional<EmailVerificationLogsJpaEntity> findByVerificationCodeAndEmail(
            String verificationCode, String email);

    Optional<EmailVerificationLogsJpaEntity> findByEmailAndIsVerified(
            String email, Boolean isVerified);

    @Modifying
    @Query("DELETE FROM EmailVerificationLogsJpaEntity e WHERE e.email = :email")
    void deleteAllByEmail(@Param("email") String email);
}
