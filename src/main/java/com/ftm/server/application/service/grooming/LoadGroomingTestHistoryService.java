package com.ftm.server.application.service.grooming;

import com.ftm.server.application.port.in.grooming.LoadGroomingTestHistoryUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestResultPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.grooming.GroomingTestHistoryVo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadGroomingTestHistoryService implements LoadGroomingTestHistoryUseCase {

    private final LoadGroomingTestResultPort loadGroomingTestResultPort;

    @Override
    public GroomingTestHistoryVo execute(FindByUserIdQuery query) {
        List<LocalDateTime> recentTestedAtList =
                loadGroomingTestResultPort.loadRecentTestedAtListByUserId(query);
        return GroomingTestHistoryVo.from(recentTestedAtList);
    }
}
