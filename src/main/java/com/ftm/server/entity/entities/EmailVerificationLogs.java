package com.ftm.server.entity.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_verification_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationLogs extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Lob
    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "trial_num")
    private Integer trialNum;

    @Column(name = "token_issuance_time")
    private LocalDateTime tokenIssuanceTime;

    @Builder(access = AccessLevel.PRIVATE)
    private EmailVerificationLogs(
            String email,
            String verificationCode,
            Boolean isVerified,
            Integer trialNum,
            LocalDateTime tokenIssuanceTime) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.trialNum = trialNum;
        this.tokenIssuanceTime = tokenIssuanceTime;
    }
}
