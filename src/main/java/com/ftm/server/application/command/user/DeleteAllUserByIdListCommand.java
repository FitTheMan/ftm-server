package com.ftm.server.application.command.user;

import java.util.*;
import lombok.Data;

@Data
public class DeleteAllUserByIdListCommand {
    private final List<Long> userIdList;

    public static DeleteAllUserByIdListCommand of(List<Long> userIdList) {
        return new DeleteAllUserByIdListCommand(userIdList);
    }
}
