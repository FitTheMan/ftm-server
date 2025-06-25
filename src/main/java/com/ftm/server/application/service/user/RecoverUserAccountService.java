package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.RecoverUserAccountCommand;
import com.ftm.server.application.port.in.user.RecoverUserAccountUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadEmailVerificationLogPort;
import com.ftm.server.application.port.out.persistence.user.LoadUserPort;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.vo.user.RecoverUserAccountVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecoverUserAccountService implements RecoverUserAccountUseCase {

    private final LoadUserPort loadUserPort;
    private final UpdateUserPort updateUserPort;

    private final LoadEmailVerificationLogPort loadEmailVerificationLogPort;

    @Override
    public RecoverUserAccountVo execute(RecoverUserAccountCommand command) {

        // 1. 이메일 인증 마쳤는지 검사
        Optional<EmailVerificationLogs> optionalEmailVerificationLogs =
                loadEmailVerificationLogPort.loadEmailVerificationLogByEmail(
                        FindByEmailQuery.of(command.getEmail()));
        // 검증 진행 전이거나, 검증이 완료되지 않은 경우 복구 불가
        if (optionalEmailVerificationLogs.isEmpty()
                || !optionalEmailVerificationLogs.get().getIsVerified()) {
            throw new CustomException(ErrorResponseCode.RECOVERING_IS_NOT_AVAILABLE);
        }

        // 2. soft delete 된 user 조회
        Optional<User> optionalUser =
                loadUserPort.loadDeletedUserByEmail(FindByEmailQuery.of(command.getEmail()));
        // 삭제되지 않은 회원에 대해 복구 시도하는 경우 복구 불가
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorResponseCode.RECOVERING_IS_NOT_AVAILABLE);
        }

        // 3. soft delete된 user 복구
        User user = optionalUser.get();
        user.updateIsDeleted(false);
        user.updateDeletedAt(null);

        updateUserPort.updateUser(user);

        return RecoverUserAccountVo.of(user.getId());
    }
}
