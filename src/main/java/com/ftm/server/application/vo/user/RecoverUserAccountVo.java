package com.ftm.server.application.vo.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecoverUserAccountVo {
    private final Long userId;

    public static RecoverUserAccountVo of(Long userId) {
        return new RecoverUserAccountVo(userId);
    }
}
