package com.ftm.server.application.service.grooming.question;

import com.ftm.server.application.command.grooming.UpdateGroomingTestQuestionCommand;
import com.ftm.server.application.port.in.grooming.question.UpdateGroomingTestQuestionUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestQuestionPort;
import com.ftm.server.application.port.out.persistence.grooming.UpdateGroomingTestQuestionPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGroomingTestQuestionService implements UpdateGroomingTestQuestionUseCase {

    private final LoadGroomingTestQuestionPort loadGroomingTestQuestionPort;
    private final UpdateGroomingTestQuestionPort updateGroomingTestQuestionPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(UpdateGroomingTestQuestionCommand command) {
        GroomingTestQuestion question =
                loadGroomingTestQuestionPort
                        .loadGroomingTestQuestionById(FindByIdQuery.of(command.getId()))
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode
                                                        .GROOMING_TEST_QUESTION_NOT_FOUND));

        // 그루밍 테스트 질문 업데이트
        question.update(command);
        updateGroomingTestQuestionPort.updateGroomingTestQuestion(question);

        // 그루밍 테스트 캐시 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
