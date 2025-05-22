package com.ftm.server.application.port.in.grooming.question;

import com.ftm.server.application.command.grooming.UpdateGroomingTestQuestionCommand;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface UpdateGroomingTestQuestionUseCase {

    void execute(UpdateGroomingTestQuestionCommand command);
}
