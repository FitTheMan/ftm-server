package com.ftm.server.application.port.in.grooming.answer;

import com.ftm.server.application.command.grooming.answer.UpdateGroomingTestAnswerCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface UpdateGroomingTestAnswerUseCase {

    void execute(UpdateGroomingTestAnswerCommand command);
}
