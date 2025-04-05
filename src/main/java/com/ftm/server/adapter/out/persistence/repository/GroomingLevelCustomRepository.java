package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import java.util.Optional;

public interface GroomingLevelCustomRepository {

    Optional<GroomingLevelJpaEntity> findByScoreInRange(int score);
}
