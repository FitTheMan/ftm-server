package com.ftm.server.application.service.user;

import com.ftm.server.application.port.in.user.EmailDuplicationCheckUseCase;
import com.ftm.server.application.port.out.persistence.user.CheckUserPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.vo.user.EmailDuplicationVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailDuplicationCheckService implements EmailDuplicationCheckUseCase {

    private final CheckUserPort checkUserPort;

    @Override
    public EmailDuplicationVo execute(FindByEmailQuery query) {
        return EmailDuplicationVo.of(checkUserPort.checksUserByEmail(query));
    }
}
