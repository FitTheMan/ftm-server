package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingLevel;

@Port
public interface SaveGroomingLevelPort {

    void saveGroomingLevel(GroomingLevel groomingLevel);
}
