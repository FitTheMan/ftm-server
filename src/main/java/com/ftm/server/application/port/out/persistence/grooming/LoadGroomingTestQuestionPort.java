package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import java.util.List;

@Port
public interface LoadGroomingTestQuestionPort {

    List<GroomingTestQuestion> loadGroomingTestQuestions();
}
