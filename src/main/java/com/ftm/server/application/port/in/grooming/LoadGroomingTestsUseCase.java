package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.Set;

@UseCase
public interface LoadGroomingTestsUseCase {

    Set<GroomingTestQuestionWithAnswersVo> execute();
}
