package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import com.ftm.server.application.port.in.grooming.SubmitGroomingTestUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingLevelPort;
import com.ftm.server.application.port.out.persistence.grooming.LoadUserForGroomingPort;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingTestResultPort;
import com.ftm.server.application.port.out.persistence.grooming.UpdateUserForGroomingPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.vo.grooming.*;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.entity.GroomingTestResult;
import com.ftm.server.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitGroomingTestService implements SubmitGroomingTestUseCase {

    private final GroomingTestResultCalculateService groomingTestResultCalculateService;
    private final LoadGroomingLevelPort loadGroomingLevelPort;
    private final LoadUserForGroomingPort loadUserForGroomingPort;
    private final UpdateUserForGroomingPort updateUserForGroomingPort;
    private final SaveGroomingTestResultPort saveGroomingTestResultPort;

    @Override
    public GroomingTestResultVo execute(SubmitGroomingTestCommand command) {
        // 그루밍 테스트 결과
        GroomingTestResultVo results = groomingTestResultCalculateService.process(command);

        // 비로그인 유저인 경우
        if (command.getUserId() == null) {
            return results;
        }

        User user =
                loadUserForGroomingPort
                        .loadUser(FindByIdQuery.of(command.getUserId()))
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 유저 그루밍 레벨 업데이트
        updateUserGroomingInfo(user, results.getScores(), results.getLevel());

        // 유저 그루밍 테스트 결과 등록
        saveUserGroomingTestResult(user.getId(), command.getSubmissions(), results);

        return results;
    }

    private void updateUserGroomingInfo(
            User user, GroomingTestResultScoresVo scores, GroomingLevelVo level) {
        user.updateGroomingInfo(scores.getTotalScore(), level.getGroomingLevelId());

        updateUserForGroomingPort.updateUserGroomingStatus(user);
    }

    private void saveUserGroomingTestResult(
            Long userId,
            List<SubmitGroomingTestCommand.SubmittedQuestion> submissions,
            GroomingTestResultVo results) {
        // 그루밍 테스트 결과 등록
        LocalDateTime testedAt = LocalDateTime.now();
        List<GroomingTestResult> groomingTestResults =
                submissions.stream()
                        .flatMap(
                                result ->
                                        result.getAnswers().stream()
                                                .map(
                                                        answerInfo ->
                                                                GroomingTestResult.create(
                                                                        userId,
                                                                        result.getQuestionId(),
                                                                        answerInfo.getAnswerId(),
                                                                        testedAt)))
                        .toList();
        saveGroomingTestResultPort.saveGroomingTestResults(groomingTestResults);
    }
}
