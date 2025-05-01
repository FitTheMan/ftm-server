package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.command.user.DeleteUserImageByUserIdCommand;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface DeleteUserImagePort {
    List<String> deleteUserImageByUserList(DeleteUserImageByUserIdCommand command);
}
