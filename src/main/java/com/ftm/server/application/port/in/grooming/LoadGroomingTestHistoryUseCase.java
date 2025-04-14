package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.grooming.GroomingTestHistoryVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface LoadGroomingTestHistoryUseCase {

    GroomingTestHistoryVo execute(FindByUserIdQuery query);
}
