package com.ftm.server.application.port.out.persistence.auth;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingLevel;
import java.util.Optional;

@Port
public interface LoadGroomingLevelForAuthPort {

    Optional<GroomingLevel> loadGroomingLevelById(FindByIdQuery query);
}
