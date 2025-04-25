package com.ftm.server.application.port.in.post;

import com.ftm.server.application.command.post.UpdatePostCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface UpdatePostUseCase {

    void execute(UpdatePostCommand command);
}
