package com.ftm.server.application.service.user;

import com.ftm.server.application.port.in.user.EmailCodeVerificationUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadEmailVerificationLogPort;
import com.ftm.server.application.port.out.persistence.user.UpdateEmailVerificationLogPort;
import com.ftm.server.application.query.EmailCodeVerificationQuery;
import com.ftm.server.application.vo.user.EmailCodeVerificationVo;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailCodeVerificationService implements EmailCodeVerificationUseCase {

    private final LoadEmailVerificationLogPort loadEmailVerificationLogPort;
    private final UpdateEmailVerificationLogPort updateEmailVerificationLogPort;

    @Override
    @Transactional
    public EmailCodeVerificationVo execute(EmailCodeVerificationQuery query) {

        Optional<EmailVerificationLogs> emailVerificationLogs =
                loadEmailVerificationLogPort.loadEmailVerificationLogByEmailAndCode(query);

        if (emailVerificationLogs.isEmpty()) { // 검증 코드가 일치하지 않음
            return EmailCodeVerificationVo.of(false);
        }
        emailVerificationLogs.get().updateVerificationStatus(true); // 검증 코드가 일치함
        updateEmailVerificationLogPort.updateEmailVerificationLog(emailVerificationLogs.get());
        return EmailCodeVerificationVo.of(true);
    }
}
