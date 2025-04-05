package com.ftm.server.adapter.out.persistence.adapter.grooming;

import com.ftm.server.adapter.out.persistence.mapper.*;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.port.out.persistence.grooming.*;
import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class GroomingDomainPersistenceAdapter
        implements LoadGroomingTestQuestionPort,
                LoadGroomingTestAnswerPort,
                LoadUserForGroomingPort,
                SaveGroomingTestResultPort,
                LoadGroomingLevelPort,
                UpdateUserForGroomingPort {

    // Repository
    private final GroomingTestQuestionRepository groomingTestQuestionRepository;
    private final GroomingTestAnswerRepository groomingTestAnswerRepository;
    private final GroomingLevelRepository groomingLevelRepository;
    private final GroomingTestResultRepository groomingTestResultRepository;
    private final UserRepository userRepository;

    // Mapper
    private final GroomingTestQuestionMapper groomingTestQuestionMapper;
    private final GroomingTestAnswerMapper groomingTestAnswerMapper;
    private final GroomingLevelMapper groomingLevelMapper;
    private final GroomingTestResultMapper groomingTestResultMapper;
    private final UserMapper userMapper;

    @Override
    public List<GroomingTestQuestion> loadGroomingTestQuestions() {
        List<GroomingTestQuestionJpaEntity> questions = groomingTestQuestionRepository.findAll();
        return questions.stream()
                .map(groomingTestQuestionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroomingTestAnswer> loadGroomingTestAnswers() {
        List<GroomingTestAnswerJpaEntity> answers = groomingTestAnswerRepository.findAll();
        return answers.stream()
                .map(groomingTestAnswerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> loadUser(FindByIdQuery query) {
        return userRepository.findById(query.getId()).map(userMapper::toDomainEntity);
    }

    @Override
    public void saveGroomingTestResults(List<GroomingTestResult> results) {
        List<GroomingTestResultJpaEntity> entities =
                results.stream()
                        .map(
                                result -> {
                                    UserJpaEntity user =
                                            userRepository.getReferenceById(result.getUserId());
                                    GroomingTestQuestionJpaEntity question =
                                            groomingTestQuestionRepository.getReferenceById(
                                                    result.getGroomingTestQuestionId());
                                    GroomingTestAnswerJpaEntity answer =
                                            groomingTestAnswerRepository.getReferenceById(
                                                    result.getGroomingTestAnswerId());

                                    return groomingTestResultMapper.toJpaEntity(
                                            user, question, answer, result);
                                })
                        .toList();

        groomingTestResultRepository.saveAll(entities);
    }

    @Override
    public Optional<GroomingLevel> loadGroomingLevelByScore(FIndGroomingLevelByScoreQuery query) {
        return groomingLevelRepository
                .findByScoreInRange(query.getScore())
                .map(groomingLevelMapper::toDomainEntity);
    }

    @Override
    public void updateUserGroomingStatus(User user) {
        UserJpaEntity userJpaEntity =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 유저 점수 업데이트
        userJpaEntity.updateGroomingScore(user);

        GroomingLevelJpaEntity groomingLevelJpaEntity =
                groomingLevelRepository
                        .findById(user.getGroomingLevelId())
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND));

        // 유저 그루밍 레벨 업데이트
        userJpaEntity.updateGroomingLevel(groomingLevelJpaEntity);
    }
}
