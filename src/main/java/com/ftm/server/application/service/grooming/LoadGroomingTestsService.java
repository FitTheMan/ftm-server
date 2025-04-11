package com.ftm.server.application.service.grooming;

import com.ftm.server.application.port.in.grooming.LoadGroomingTestsUseCase;
import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadGroomingTestsService implements LoadGroomingTestsUseCase {

    private final LoadGroomingTestsWithCachePort loadGroomingTestsWithCachePort;

    @Override
    @Transactional(readOnly = true)
    public List<GroomingTestQuestionWithAnswersVo> execute() {

        // 캐싱된 그루밍 테스트 목록 조회
        List<GroomingTestQuestionWithAnswersVo> infos =
                loadGroomingTestsWithCachePort.loadGroomingTestsCache();

        // 그루밍 테스트 목록의 순서를 랜덤하게 섞는 작업
        Collections.shuffle(infos);

        return infos;
    }
}
