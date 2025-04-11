package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.List;

@UseCase
public interface LoadGroomingTestsUseCase {

    List<GroomingTestQuestionWithAnswersVo> execute();
}
