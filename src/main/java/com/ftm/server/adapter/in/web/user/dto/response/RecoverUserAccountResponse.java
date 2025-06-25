package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.user.RecoverUserAccountVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecoverUserAccountResponse {
    private final Long userId;

    public static RecoverUserAccountResponse from(RecoverUserAccountVo vo) {
        return new RecoverUserAccountResponse(vo.getUserId());
    }
}
