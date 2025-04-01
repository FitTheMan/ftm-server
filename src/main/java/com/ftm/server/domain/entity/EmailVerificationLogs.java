package com.ftm.server.domain.entity;

import com.ftm.server.application.command.user.EmailVerificationLogCreationCommand;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationLogs extends BaseTime {

    private Long id;
    private String email;
    private String verificationCode;
    private Boolean isVerified = false;
    private Integer trialNum;
    private LocalDateTime tokenIssuanceTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private EmailVerificationLogs(
            Long id,
            String email,
            String verificationCode,
            Boolean isVerified,
            Integer trialNum,
            LocalDateTime tokenIssuanceTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.trialNum = trialNum;
        this.tokenIssuanceTime = tokenIssuanceTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EmailVerificationLogs of(
            Long id,
            String email,
            String verificationCode,
            Boolean isVerified,
            Integer trialNum,
            LocalDateTime tokenIssuanceTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return EmailVerificationLogs.builder()
                .id(id)
                .email(email)
                .verificationCode(verificationCode)
                .isVerified(isVerified)
                .trialNum(trialNum)
                .tokenIssuanceTime(tokenIssuanceTime)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static EmailVerificationLogs from(EmailVerificationLogCreationCommand command) {
        return EmailVerificationLogs.builder()
                .email(command.getEmail())
                .verificationCode(command.getVerificationCode())
                .isVerified(false)
                .trialNum(1)
                .tokenIssuanceTime(LocalDateTime.now())
                .build();
    }

    public void updateVerificationStatus(String verificationCode) {
        this.trialNum++;
        this.verificationCode = verificationCode;
        this.tokenIssuanceTime = LocalDateTime.now();
    }

    public void initializeVerificationStatus(String verificationCode) {
        this.trialNum = 1;
        this.verificationCode = verificationCode;
        this.tokenIssuanceTime = LocalDateTime.now();
    }

    public void updateVerificationStatus(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
