package com.ftm.server.adapter.out.persistence.adapter.grooming;

import com.ftm.server.adapter.out.persistence.mapper.GroomingTestAnswerMapper;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestQuestionMapper;
import com.ftm.server.adapter.out.persistence.model.GroomingTestAnswerJpaEntity;
import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestAnswerRepository;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestQuestionRepository;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestAnswerPort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestQuestionPort;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class GroomingDomainPersistenceAdapter
        implements LoadGroomingTestQuestionPort, LoadGroomingTestAnswerPort {

    // Repository
    private final GroomingTestQuestionRepository groomingTestQuestionRepository;
    private final GroomingTestAnswerRepository groomingTestAnswerRepository;

    // Mapper
    private final GroomingTestQuestionMapper groomingTestQuestionMapper;
    private final GroomingTestAnswerMapper groomingTestAnswerMapper;

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
}
