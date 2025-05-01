package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.DeleteAllUserByIdListCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteUserPort {
    void deleteAllUserByIdList(DeleteAllUserByIdListCommand command);
}
