package com.ftm.server.application.port.in.post;

import com.ftm.server.application.command.post.DeletePostCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface DeletePostUseCase {

    void execute(DeletePostCommand command);
}
