package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class DeleteBookmarkByIdCommand {
    private final Long bookmarkId;

    public static DeleteBookmarkByIdCommand of(Long bookmarkId) {
        return new DeleteBookmarkByIdCommand(bookmarkId);
    }
}
