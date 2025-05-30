package com.ftm.server.application.port.in.grooming.level;

import com.ftm.server.application.command.grooming.level.UpdateGroomingLevelCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface UpdateGroomingLevelUseCase {

    void execute(UpdateGroomingLevelCommand command);
}
