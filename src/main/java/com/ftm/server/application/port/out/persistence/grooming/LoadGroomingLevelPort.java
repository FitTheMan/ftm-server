package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingLevel;
import java.util.Optional;

@Port
public interface LoadGroomingLevelPort {

    Optional<GroomingLevel> loadGroomingLevelByScore(FIndGroomingLevelByScoreQuery query);
}
