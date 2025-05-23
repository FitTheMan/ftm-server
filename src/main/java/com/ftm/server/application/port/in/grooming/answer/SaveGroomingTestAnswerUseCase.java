package com.ftm.server.application.port.in.grooming.answer;

import com.ftm.server.application.command.grooming.answer.SaveGroomingTestAnswerCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SaveGroomingTestAnswerUseCase {

    void execute(SaveGroomingTestAnswerCommand command);
}
