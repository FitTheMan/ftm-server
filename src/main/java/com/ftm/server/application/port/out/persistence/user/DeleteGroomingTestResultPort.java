package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.DeleteGroomingTestResultByUserIdCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteGroomingTestResultPort {
    void deleteGroomingTestResultByUserList(DeleteGroomingTestResultByUserIdCommand command);
}
