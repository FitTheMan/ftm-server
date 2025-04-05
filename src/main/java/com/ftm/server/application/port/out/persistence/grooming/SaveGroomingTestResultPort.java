package com.ftm.server.application.port.out.persistence.grooming;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.GroomingTestResult;
import java.util.List;

@Port
public interface SaveGroomingTestResultPort {

    void saveGroomingTestResults(List<GroomingTestResult> results);
}
