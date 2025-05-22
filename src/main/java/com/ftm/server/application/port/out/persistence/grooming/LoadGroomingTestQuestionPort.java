package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadGroomingTestQuestionPort {

    List<GroomingTestQuestion> loadGroomingTestQuestions();

    Optional<GroomingTestQuestion> loadGroomingTestQuestionById(FindByIdQuery query);
}
