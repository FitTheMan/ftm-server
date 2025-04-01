package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.EmailVerificationLogs;

@Port
public interface UpdateEmailVerificationLogPort {
    void updateEmailVerificationLog(EmailVerificationLogs emailVerificationLogs);
}
