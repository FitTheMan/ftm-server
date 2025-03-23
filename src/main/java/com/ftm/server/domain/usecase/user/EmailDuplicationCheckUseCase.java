package com.ftm.server.domain.usecase.user;

import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.dto.query.FindByEmailQuery;
import com.ftm.server.domain.dto.vo.EmailDuplicationVo;
import com.ftm.server.domain.service.UserService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class EmailDuplicationCheckUseCase {

    private final UserService userService;

    public EmailDuplicationVo emailDuplicationCheck(FindByEmailQuery query) {
        return userService.isEmailDuplicated(query);
    }
}
