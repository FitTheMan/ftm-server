package com.ftm.server.application.port.in.grooming.level;

import com.ftm.server.application.command.grooming.level.DeleteGroomingLevelCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface DeleteGroomingLevelUseCase {

    void execute(DeleteGroomingLevelCommand command);
}
