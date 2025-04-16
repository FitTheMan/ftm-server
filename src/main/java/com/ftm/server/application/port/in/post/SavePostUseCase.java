package com.ftm.server.application.port.in.post;

import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SavePostUseCase {

    void execute(SavePostCommand command);
}
