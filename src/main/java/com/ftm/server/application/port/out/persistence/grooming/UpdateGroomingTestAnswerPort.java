package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestAnswer;

@Port
public interface UpdateGroomingTestAnswerPort {

    void updateGroomingTestAnswer(
            GroomingTestAnswer groomingTestAnswer, boolean isQuestionModified);
}
