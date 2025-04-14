package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.ftm.server.application.vo.grooming.GroomingTestHistoryDetailVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface LoadGroomingTestHistoryDetailUseCase {

    GroomingTestHistoryDetailVo execute(FindGroomingTestResultByUserIdAndTestedAtQuery query);
}
