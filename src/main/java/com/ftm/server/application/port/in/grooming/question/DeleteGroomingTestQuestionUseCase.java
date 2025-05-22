package com.ftm.server.application.port.in.grooming.question;

import com.ftm.server.application.command.grooming.DeleteGroomingTestQuestionCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface DeleteGroomingTestQuestionUseCase {

    void execute(DeleteGroomingTestQuestionCommand command);
}
