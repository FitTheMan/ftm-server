package com.ftm.server.adapter.dto.response;

import com.ftm.server.domain.dto.vo.EmailDuplicationVo;
import lombok.Data;

@Data
public class EmailDuplicationCheckResponse {

    private final Boolean isDuplicated;

    public static EmailDuplicationCheckResponse from(EmailDuplicationVo emailDuplicationVo) {
        return new EmailDuplicationCheckResponse(emailDuplicationVo.getIsDuplicated());
    }
}
