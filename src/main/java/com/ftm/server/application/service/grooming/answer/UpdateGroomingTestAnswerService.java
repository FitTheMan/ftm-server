package com.ftm.server.application.service.grooming.answer;

import com.ftm.server.application.command.grooming.answer.UpdateGroomingTestAnswerCommand;
import com.ftm.server.application.port.in.grooming.answer.UpdateGroomingTestAnswerUseCase;
import com.ftm.server.application.port.out.cache.InvalidGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestAnswerPort;
import com.ftm.server.application.port.out.persistence.grooming.UpdateGroomingTestAnswerPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGroomingTestAnswerService implements UpdateGroomingTestAnswerUseCase {

    private final LoadGroomingTestAnswerPort loadGroomingTestAnswerPort;
    private final UpdateGroomingTestAnswerPort updateGroomingTestAnswerPort;
    private final InvalidGroomingTestsWithCachePort invalidGroomingTestsWithCachePort;

    @Transactional
    @Override
    public void execute(UpdateGroomingTestAnswerCommand command) {
        GroomingTestAnswer answer =
                loadGroomingTestAnswerPort
                        .loadGroomingTestAnswerById(FindByIdQuery.of(command.getId()))
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.GROOMING_TEST_ANSWER_NOT_FOUND));

        // 답변이 속한 질문 변경 여부
        boolean isQuestionModified = command.getQuestionId() != null;

        // 답변 업데이트
        answer.update(command);
        updateGroomingTestAnswerPort.updateGroomingTestAnswer(answer, isQuestionModified);

        // 그루밍 테스트 캐싱 목록 초기화
        invalidGroomingTestsWithCachePort.invalidGroomingTestsCache();
    }
}
