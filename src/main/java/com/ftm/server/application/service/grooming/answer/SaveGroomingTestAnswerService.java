package com.ftm.server.application.service.grooming.answer;

import com.ftm.server.application.command.grooming.answer.SaveGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.SaveGroomingTestAnswerUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingTestAnswerPort;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveGroomingTestAnswerService implements SaveGroomingTestAnswerUseCase {

    private final SaveGroomingTestAnswerPort saveGroomingTestAnswerPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(SaveGroomingTestAnswerCommand command) {
        GroomingTestAnswer newAnswer = GroomingTestAnswer.create(command);
        saveGroomingTestAnswerPort.saveGroomingTestAnswer(newAnswer);

        // 그루밍 테스트 캐싱 목록 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
