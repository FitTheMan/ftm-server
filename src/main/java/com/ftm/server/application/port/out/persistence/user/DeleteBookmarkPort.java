package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.DeleteBookmarkByUserIdCommand;
import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteBookmarkPort {
    void deleteBookmarkByUserList(DeleteBookmarkByUserIdCommand command);
}
