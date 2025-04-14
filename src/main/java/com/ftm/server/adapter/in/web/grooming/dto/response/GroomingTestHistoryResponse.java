package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.ftm.server.application.vo.grooming.GroomingTestHistoryVo;
import java.util.List;
import lombok.Getter;

@Getter
public class GroomingTestHistoryResponse {

    private final List<String> historyDates;

    private GroomingTestHistoryResponse(GroomingTestHistoryVo groomingTestHistoryVo) {
        this.historyDates = groomingTestHistoryVo.getHistoryDates();
    }

    public static GroomingTestHistoryResponse of(GroomingTestHistoryVo groomingTestHistoryVo) {
        return new GroomingTestHistoryResponse(groomingTestHistoryVo);
    }
}
