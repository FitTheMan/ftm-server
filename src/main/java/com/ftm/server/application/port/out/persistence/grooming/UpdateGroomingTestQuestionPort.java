package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestQuestion;

@Port
public interface UpdateGroomingTestQuestionPort {

    void updateGroomingTestQuestion(GroomingTestQuestion groomingTestQuestion);
}
