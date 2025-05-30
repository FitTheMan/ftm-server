package com.ftm.server.application.service.grooming.question;

import com.ftm.server.application.command.grooming.question.SaveGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.SaveGroomingTestQuestionUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingTestQuestionPort;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveGroomingTestQuestionService implements SaveGroomingTestQuestionUseCase {

    private final SaveGroomingTestQuestionPort saveGroomingTestQuestionPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(SaveGroomingTestQuestionCommand command) {
        GroomingTestQuestion groomingTestQuestion = GroomingTestQuestion.create(command);

        // 그루밍 테스트 질문 저장
        saveGroomingTestQuestionPort.saveGroomingTestQuestion(groomingTestQuestion);

        // 그루밍 테스트 캐싱 정보 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
