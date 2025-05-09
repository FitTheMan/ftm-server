package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByPostIdQuery;

public interface CheckPostPort {
    Boolean checksPostById(FindByPostIdQuery query);
}
