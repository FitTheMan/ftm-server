package com.ftm.server.application.usecase.user;

import com.ftm.server.application.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.application.service.EmailVerificationLogsService;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.vo.EmailCodeVerificationVo;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class EmailCodeVerificationUseCase {

    private final EmailVerificationLogsService emailVerificationLogsService;

    @Transactional
    public EmailCodeVerificationVo execute(EmailCodeVerificationQuery query) {

        Optional<EmailVerificationLogs> emailVerificationLogs =
                emailVerificationLogsService.findByAuthenticationCode(query);

        if (emailVerificationLogs.isEmpty()) { // 검증 코드가 일치하지 않음
            return EmailCodeVerificationVo.of(false);
        }
        emailVerificationLogs.get().updateVerificationStatus(true); // 검증 코드가 일치함
        return EmailCodeVerificationVo.of(true);
    }
}
