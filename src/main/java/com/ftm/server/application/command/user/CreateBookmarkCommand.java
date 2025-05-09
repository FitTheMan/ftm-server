package com.ftm.server.application.command.user;

import lombok.Data;

@Data
public class CreateBookmarkCommand {
    private final Long userId;
    private final Long postId;

    public static CreateBookmarkCommand of(Long userId, Long postId) {
        return new CreateBookmarkCommand(userId, postId);
    }
}
