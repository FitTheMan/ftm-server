package com.ftm.server.application.service.grooming.answer;

import com.ftm.server.application.command.grooming.answer.DeleteGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.DeleteGroomingTestAnswerUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.DeleteGroomingTestAnswerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGroomingTestAnswerService implements DeleteGroomingTestAnswerUseCase {

    private final DeleteGroomingTestAnswerPort deleteGroomingTestAnswerPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(DeleteGroomingTestAnswerCommand command) {
        // 답변 삭제
        deleteGroomingTestAnswerPort.deleteGroomingTestAnswerById(command.getId());

        // 그루밍 테스트 캐싱 목록 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
