package com.ftm.server.application.command.user;

import java.util.List;
import lombok.Data;

@Data
public class DeleteBookmarkByUserIdCommand {
    private final List<Long> userIdList;

    public static DeleteBookmarkByUserIdCommand of(List<Long> userIdList) {
        return new DeleteBookmarkByUserIdCommand(userIdList);
    }
}
