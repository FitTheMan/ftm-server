package com.ftm.server.application.service.grooming;

import com.ftm.server.application.command.grooming.SaveGroomingTestResultCommand;
import com.ftm.server.application.port.in.grooming.SaveGroomingTestResultUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadUserForGroomingPort;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingTestResultPort;
import com.ftm.server.application.port.out.persistence.grooming.UpdateUserForGroomingPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.vo.grooming.SubmitGroomingTestVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.entity.GroomingTestResult;
import com.ftm.server.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveGroomingTestResultService implements SaveGroomingTestResultUseCase {

    private final SaveGroomingTestResultPort saveGroomingTestResultPort;
    private final UpdateUserForGroomingPort updateUserForGroomingPort;
    private final LoadUserForGroomingPort loadUserForGroomingPort;

    private final GroomingTestValidator groomingTestValidator;

    @Override
    @Transactional
    public void execute(SaveGroomingTestResultCommand command) {
        // 그루밍 테스트 유효성 검증
        List<SubmitGroomingTestVo> submissions = SubmitGroomingTestVo.from(command);
        groomingTestValidator.execute(submissions);

        User user =
                loadUserForGroomingPort
                        .loadUser(FindByIdQuery.of(command.getUserId()))
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 그루밍 테스트 결과 저장
        saveUserGroomingTestResult(command);

        // 유저 그루밍 정보 업데이트
        updateUserGroomingInfo(user, command);
    }

    private void saveUserGroomingTestResult(SaveGroomingTestResultCommand command) {
        // 그루밍 테스트 결과 등록
        LocalDateTime testedAt = LocalDateTime.now();
        List<GroomingTestResult> groomingTestResults =
                command.getResults().stream()
                        .flatMap(
                                result ->
                                        result.getAnswerIds().stream()
                                                .map(
                                                        answerId ->
                                                                GroomingTestResult.create(
                                                                        command.getUserId(),
                                                                        result.getQuestionId(),
                                                                        answerId,
                                                                        testedAt)))
                        .toList();
        saveGroomingTestResultPort.saveGroomingTestResults(groomingTestResults);
    }

    private void updateUserGroomingInfo(User user, SaveGroomingTestResultCommand command) {
        user.updateGroomingInfo(command.getTotalScore(), command.getGroomingLevelId());

        updateUserForGroomingPort.updateUserGroomingStatus(user);
    }
}
