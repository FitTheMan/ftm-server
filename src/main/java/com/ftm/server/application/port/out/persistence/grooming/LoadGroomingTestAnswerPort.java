package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import java.util.List;

@Port
public interface LoadGroomingTestAnswerPort {

    List<GroomingTestAnswer> loadGroomingTestAnswers();
}
