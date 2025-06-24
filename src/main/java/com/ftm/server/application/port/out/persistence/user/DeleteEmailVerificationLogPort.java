package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.DeleteByEmailCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteEmailVerificationLogPort {
    void deleteEmailVerificationLogsByEmail(DeleteByEmailCommand command);
}
