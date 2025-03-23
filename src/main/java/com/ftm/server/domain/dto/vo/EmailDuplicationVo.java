package com.ftm.server.domain.dto.vo;

import lombok.Data;

@Data
public class EmailDuplicationVo {
    private final Boolean isDuplicated;

    public static EmailDuplicationVo of(Boolean isDuplicated) {
        return new EmailDuplicationVo(isDuplicated);
    }
}
