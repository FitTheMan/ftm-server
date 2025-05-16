package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.DeleteBookmarkCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface DeleteBookmarkUseCase {
    void execute(DeleteBookmarkCommand command);
}
