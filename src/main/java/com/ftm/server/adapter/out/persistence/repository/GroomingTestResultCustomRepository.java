package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.application.query.FindByUserIdQuery;
import java.time.LocalDateTime;

public interface GroomingTestResultCustomRepository {

    LocalDateTime fetchLatestTestedAtByUserId(FindByUserIdQuery query);
}
