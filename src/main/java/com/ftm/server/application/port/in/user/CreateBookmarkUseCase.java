package com.ftm.server.application.port.in.user;

import com.ftm.server.application.command.user.CreateBookmarkCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface CreateBookmarkUseCase {
    Boolean execute(CreateBookmarkCommand command);
}
