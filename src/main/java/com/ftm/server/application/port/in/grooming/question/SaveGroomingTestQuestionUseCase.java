package com.ftm.server.application.port.in.grooming.question;

import com.ftm.server.application.command.grooming.SaveGroomingTestQuestionCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SaveGroomingTestQuestionUseCase {

    void execute(SaveGroomingTestQuestionCommand command);
}
