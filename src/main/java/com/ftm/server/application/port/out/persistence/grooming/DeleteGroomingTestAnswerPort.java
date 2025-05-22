package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteGroomingTestAnswerPort {

    void deleteGroomingTestAnswersByQuestionId(Long questionId);
}
