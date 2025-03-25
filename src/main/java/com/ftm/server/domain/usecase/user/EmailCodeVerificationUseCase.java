package com.ftm.server.domain.usecase.user;

import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.dto.query.EmailCodeVerificationQuery;
import com.ftm.server.domain.dto.vo.EmailCodeVerificationVo;
import com.ftm.server.domain.service.EmailVerificationLogsService;
import com.ftm.server.entity.entities.EmailVerificationLogs;
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
