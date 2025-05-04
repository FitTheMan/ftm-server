package com.ftm.server.application.command.post;

import lombok.Getter;

@Getter
public class DeletePostCommand {

    private final Long postId;
    private final Long userId;

    private DeletePostCommand(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public static DeletePostCommand of(Long postId, Long userId) {
        return new DeletePostCommand(postId, userId);
    }
}
