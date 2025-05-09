package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindBookmarkByUserIdAndPostIdQuery;
import com.ftm.server.common.annotation.Port;

@Port
public interface CheckBookmarkPort {

    Boolean checkIfBookmarkExists(FindBookmarkByUserIdAndPostIdQuery query);
}
