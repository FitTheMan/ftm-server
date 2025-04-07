package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Port;
import java.time.LocalDateTime;

@Port
public interface LoadGroomingTestResultPort {

    LocalDateTime loadLatestTestedAtByUserId(FindByUserIdQuery query);
}
