package com.ftm.server.application.usecase.user;

import com.ftm.server.application.dto.query.FindByEmailQuery;
import com.ftm.server.application.service.UserService;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.vo.EmailDuplicationVo;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class EmailDuplicationCheckUseCase {

    private final UserService userService;

    public EmailDuplicationVo emailDuplicationCheck(FindByEmailQuery query) {
        return userService.isEmailDuplicated(query);
    }
}
