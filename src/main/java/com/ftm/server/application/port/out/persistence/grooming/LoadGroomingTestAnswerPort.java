package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadGroomingTestAnswerPort {

    List<GroomingTestAnswer> loadGroomingTestAnswers();

    Optional<GroomingTestAnswer> loadGroomingTestAnswerById(FindByIdQuery query);
}
