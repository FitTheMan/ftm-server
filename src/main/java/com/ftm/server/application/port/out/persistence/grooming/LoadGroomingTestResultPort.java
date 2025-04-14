package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestResult;
import java.time.LocalDateTime;
import java.util.List;

@Port
public interface LoadGroomingTestResultPort {

    LocalDateTime loadLatestTestedAtByUserId(FindByUserIdQuery query);

    List<LocalDateTime> loadRecentTestedAtListByUserId(FindByUserIdQuery query);

    List<GroomingTestResult> loadByUserIdAndTestedAt(
            FindGroomingTestResultByUserIdAndTestedAtQuery query);
}
