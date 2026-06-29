package com.ftm.server.grooming.level;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.out.persistence.mapper.GroomingLevelMapper;
import com.ftm.server.adapter.out.persistence.repository.GroomingLevelRepository;
import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class DeleteGroomingLevelTest extends BaseTest {

    @Autowired private GroomingLevelRepository groomingLevelRepository;
    @Autowired private GroomingLevelMapper groomingLevelMapper;

    private Long levelId;

    private ResultActions getResultActions(Long levelId, MockHttpSession session) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/grooming/levels/{levelId}", levelId)
                        .session(session));
    }

    @BeforeEach
    void setUp() {
        SaveGroomingLevelCommand command =
                SaveGroomingLevelCommand.of(
                        0,
                        5,
                        "레벨 일반모드 이름",
                        "레벨 일반모드 요약",
                        "레벨 일반모드 설명",
                        "레벨 진심모드 이름",
                        "레벨 진심모드 요약",
                        "레벨 진심모드 설명",
                        "https://test-image-path");
        GroomingLevel level = GroomingLevel.create(command);
        levelId = groomingLevelRepository.save(groomingLevelMapper.toJpaEntity(level)).getId();
    }

    @Test
    @Transactional
    void 그루밍_레벨_삭제_성공() throws Exception {
        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(levelId, session);

        // given
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_레벨_삭제_실패() throws Exception {
        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(levelId, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }
}
