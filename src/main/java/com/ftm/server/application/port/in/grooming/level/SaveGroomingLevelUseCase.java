package com.ftm.server.application.port.in.grooming.level;

import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SaveGroomingLevelUseCase {

    void execute(SaveGroomingLevelCommand command);
}
