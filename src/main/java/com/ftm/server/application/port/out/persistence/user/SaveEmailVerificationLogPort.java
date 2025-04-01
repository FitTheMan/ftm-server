package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.EmailVerificationLogCreationCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface SaveEmailVerificationLogPort {
    void saveEmailVerificationLogs(EmailVerificationLogCreationCommand command);
}
