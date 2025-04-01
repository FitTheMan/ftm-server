package com.ftm.server.adapter.out.persistence.mapper;

import com.ftm.server.adapter.out.persistence.model.EmailVerificationLogsJpaEntity;
import com.ftm.server.common.annotation.EntityMapper;
import com.ftm.server.domain.entity.EmailVerificationLogs;

@EntityMapper
public class EmailVerificationLogsMapper {

    public EmailVerificationLogs toDomainEntity(EmailVerificationLogsJpaEntity jpaEntity) {

        return EmailVerificationLogs.of(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getVerificationCode(),
                jpaEntity.getIsVerified(),
                jpaEntity.getTrialNum(),
                jpaEntity.getTokenIssuanceTime(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());
    }

    public EmailVerificationLogsJpaEntity toJpaEntity(EmailVerificationLogs domainEntity) {
        return EmailVerificationLogsJpaEntity.from(domainEntity);
    }
}
