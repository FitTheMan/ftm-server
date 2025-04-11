package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadGroomingTestsWithCachePort {

    List<GroomingTestQuestionWithAnswersVo> loadGroomingTestsCache();
}
