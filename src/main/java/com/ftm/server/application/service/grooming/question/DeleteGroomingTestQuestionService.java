package com.ftm.server.application.service.grooming.question;

import com.ftm.server.application.command.grooming.question.DeleteGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.DeleteGroomingTestQuestionUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.DeleteGroomingTestAnswerPort;
import com.ftm.server.application.port.out.persistence.grooming.DeleteGroomingTestQuestionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGroomingTestQuestionService implements DeleteGroomingTestQuestionUseCase {

    private final DeleteGroomingTestAnswerPort deleteGroomingTestAnswerPort;
    private final DeleteGroomingTestQuestionPort deleteGroomingTestQuestionPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(DeleteGroomingTestQuestionCommand command) {
        // 질문의 답변 목록 삭제
        deleteGroomingTestAnswerPort.deleteGroomingTestAnswersByQuestionId(command.getId());

        // 질문 삭제
        deleteGroomingTestQuestionPort.deleteGroomingTestQuestionById(command.getId());

        // 그루밍 테스트 캐싱 목록 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
