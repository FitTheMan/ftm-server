package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.EmailCodeVerificationQuery;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import java.util.Optional;

@Port
public interface LoadEmailVerificationLogPort {

    Optional<EmailVerificationLogs> loadEmailVerificationLogByEmail(FindByEmailQuery query);

    Optional<EmailVerificationLogs> loadEmailVerificationLogByEmailAndCode(
            EmailCodeVerificationQuery query);
}
