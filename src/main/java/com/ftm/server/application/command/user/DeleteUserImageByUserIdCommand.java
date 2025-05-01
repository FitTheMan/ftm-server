package com.ftm.server.application.command.user;

import java.util.List;
import lombok.Data;

@Data
public class DeleteUserImageByUserIdCommand {
    private final List<Long> userIdList;

    public static DeleteUserImageByUserIdCommand of(List<Long> userIdList) {
        return new DeleteUserImageByUserIdCommand(userIdList);
    }
}
