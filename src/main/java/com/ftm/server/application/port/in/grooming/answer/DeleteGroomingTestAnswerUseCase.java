package com.ftm.server.application.port.in.grooming.answer;

import com.ftm.server.application.command.grooming.answer.DeleteGroomingTestAnswerCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface DeleteGroomingTestAnswerUseCase {

    void execute(DeleteGroomingTestAnswerCommand command);
}
