package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestResultJpaEntity;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import java.time.LocalDateTime;
import java.util.List;

public interface GroomingTestResultCustomRepository {

    LocalDateTime fetchLatestTestedAtByUserId(FindByUserIdQuery query);

    List<LocalDateTime> fetchRecentTestedAtListByUserId(FindByUserIdQuery query);

    List<GroomingTestResultJpaEntity> loadByUserIdAndTestedAt(
            FindGroomingTestResultByUserIdAndTestedAtQuery query);
}
