package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.user.EmailDuplicationVo;
import lombok.Data;

@Data
public class EmailDuplicationCheckResponse {

    private final Boolean isDuplicated;

    public static EmailDuplicationCheckResponse from(EmailDuplicationVo emailDuplicationVo) {
        return new EmailDuplicationCheckResponse(emailDuplicationVo.getIsDuplicated());
    }
}
