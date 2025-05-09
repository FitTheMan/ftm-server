package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.application.query.FindBySocialValueQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Port;

@Port
public interface CheckUserPort {
    Boolean checksUserByEmail(FindByEmailQuery query);

    Boolean checksUserBySocialValue(FindBySocialValueQuery query);

    Boolean checksUserById(FindByUserIdQuery query);
}
