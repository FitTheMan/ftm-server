package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class DeleteBookmarkCommand {
    private final Long userId;
    private final Long postId;

    public static DeleteBookmarkCommand of(Long userId, Long postId) {
        return new DeleteBookmarkCommand(userId, postId);
    }
}
