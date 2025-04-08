package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.command.grooming.SaveGroomingTestResultCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SaveGroomingTestResultUseCase {

    void execute(SaveGroomingTestResultCommand command);
}
