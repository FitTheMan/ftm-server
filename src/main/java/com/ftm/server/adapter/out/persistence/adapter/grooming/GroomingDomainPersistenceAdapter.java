package com.ftm.server.adapter.out.persistence.adapter.grooming;

import com.ftm.server.adapter.out.persistence.mapper.*;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.port.out.persistence.grooming.*;
import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import java.time.LocalDateTime;
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
                UpdateUserForGroomingPort,
                LoadGroomingTestResultPort,
                SaveGroomingTestQuestionPort,
                UpdateGroomingTestQuestionPort,
                DeleteGroomingTestQuestionPort,
                DeleteGroomingTestAnswerPort,
                SaveGroomingTestAnswerPort,
                UpdateGroomingTestAnswerPort {

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
    public Optional<GroomingTestQuestion> loadGroomingTestQuestionById(FindByIdQuery query) {
        return groomingTestQuestionRepository
                .findById(query.getId())
                .map(groomingTestQuestionMapper::toDomain);
    }

    @Override
    public List<GroomingTestAnswer> loadGroomingTestAnswers() {
        List<GroomingTestAnswerJpaEntity> answers = groomingTestAnswerRepository.findAll();
        return answers.stream()
                .map(groomingTestAnswerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroomingTestAnswer> loadGroomingTestAnswerById(FindByIdQuery query) {
        return groomingTestAnswerRepository
                .findById(query.getId())
                .map(groomingTestAnswerMapper::toDomain);
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

    @Override
    public LocalDateTime loadLatestTestedAtByUserId(FindByUserIdQuery query) {
        return groomingTestResultRepository.fetchLatestTestedAtByUserId(query);
    }

    @Override
    public List<LocalDateTime> loadRecentTestedAtListByUserId(FindByUserIdQuery query) {
        return groomingTestResultRepository.fetchRecentTestedAtListByUserId(query);
    }

    @Override
    public List<GroomingTestResult> loadByUserIdAndTestedAt(
            FindGroomingTestResultByUserIdAndTestedAtQuery query) {
        List<GroomingTestResultJpaEntity> results =
                groomingTestResultRepository.loadByUserIdAndTestedAt(query);

        return results.stream().map(groomingTestResultMapper::toDomainEntity).toList();
    }

    @Override
    public void saveGroomingTestQuestion(GroomingTestQuestion groomingTestQuestion) {
        groomingTestQuestionRepository.save(
                groomingTestQuestionMapper.toJpaEntity(groomingTestQuestion));
    }

    @Override
    public void updateGroomingTestQuestion(GroomingTestQuestion groomingTestQuestion) {
        GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity =
                groomingTestQuestionRepository
                        .findById(groomingTestQuestion.getId())
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode
                                                        .GROOMING_TEST_QUESTION_NOT_FOUND));

        groomingTestQuestionJpaEntity.updateGroomingTestQuestionForDomainEntity(
                groomingTestQuestion);
    }

    @Override
    public void deleteGroomingTestQuestionById(Long id) {
        groomingTestQuestionRepository.deleteById(id);
    }

    @Override
    public void deleteGroomingTestAnswersByQuestionId(Long questionId) {
        groomingTestAnswerRepository.deleteAllByGroomingTestQuestionId(questionId);
    }

    @Override
    public void deleteGroomingTestAnswerById(Long id) {
        groomingTestAnswerRepository.deleteById(id);
    }

    @Override
    public void saveGroomingTestAnswer(GroomingTestAnswer groomingTestAnswer) {
        GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity =
                groomingTestQuestionRepository
                        .findById(groomingTestAnswer.getGroomingTestQuestionId())
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode
                                                        .GROOMING_TEST_QUESTION_NOT_FOUND));

        groomingTestAnswerRepository.save(
                groomingTestAnswerMapper.toJpaEntity(
                        groomingTestAnswer, groomingTestQuestionJpaEntity));
    }

    @Override
    public void updateGroomingTestAnswer(
            GroomingTestAnswer groomingTestAnswer, boolean isQuestionModified) {
        GroomingTestAnswerJpaEntity groomingTestAnswerJpaEntity =
                groomingTestAnswerRepository
                        .findById(groomingTestAnswer.getId())
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.GROOMING_TEST_ANSWER_NOT_FOUND));

        // 답변이 속한 질문을 수정했을 경우, 질문 정보까지 함께 수정
        if (isQuestionModified) {
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity =
                    groomingTestQuestionRepository
                            .findById(groomingTestAnswer.getGroomingTestQuestionId())
                            .orElseThrow(
                                    () ->
                                            new CustomException(
                                                    ErrorResponseCode
                                                            .GROOMING_TEST_QUESTION_NOT_FOUND));

            groomingTestAnswerJpaEntity.updateGroomingTestAnswerForDomainEntity(
                    groomingTestQuestionJpaEntity, groomingTestAnswer);
            return;
        }

        // 질문 정보를 제외한 나머지 필드 수정
        groomingTestAnswerJpaEntity.updateGroomingTestAnswerForDomainEntity(groomingTestAnswer);
    }
}
