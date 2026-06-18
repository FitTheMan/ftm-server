package com.ftm.server.grooming.level;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingLevelRequest;
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

public class UpdateGroomingLevelTest extends BaseTest {

    @Autowired private GroomingLevelRepository groomingLevelRepository;
    @Autowired private GroomingLevelMapper groomingLevelMapper;

    private Long levelId;

    private ResultActions getResultActions(
            Long levelId, UpdateGroomingLevelRequest request, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/grooming/levels/{levelId}", levelId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
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
    void 그루밍_레벨_수정_성공() throws Exception {
        // given
        UpdateGroomingLevelRequest request =
                new UpdateGroomingLevelRequest(
                        1,
                        9,
                        "레벨 일반모드 이름",
                        "레벨 일반모드 요약",
                        "레벨 일반모드 설명",
                        "레벨 진심모드 이름",
                        "레벨 진심모드 요약",
                        "레벨 진심모드 설명",
                        "https://test-image-path");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(levelId, request, session);

        // given
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_레벨_수정_실패1() throws Exception {
        // given
        UpdateGroomingLevelRequest request =
                new UpdateGroomingLevelRequest(1, 9, null, null, null, null, null, null, null);

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(levelId, request, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_레벨_수정_실패2() throws Exception {
        // given
        UpdateGroomingLevelRequest request =
                new UpdateGroomingLevelRequest(1, 9, null, null, null, null, null, null, null);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(1000000L, request, session);

        // given
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());
    }
}
