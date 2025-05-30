package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingLevel;

@Port
public interface UpdateGroomingLevelPort {

    void updateGroomingLevel(GroomingLevel groomingLevel);
}
