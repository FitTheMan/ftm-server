package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class GetUserSimpleInfoTest extends BaseTest {

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.userId")
                            .type(JsonFieldType.NUMBER)
                            .description("사용자 고유 id"),
                    fieldWithPath("data.userNickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 닉네임"),
                    fieldWithPath("data.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 프로필 이미지 url"),
                    fieldWithPath("data.ageInfo")
                            .type(JsonFieldType.OBJECT)
                            .description("사용자 연령대 정보"),
                    fieldWithPath("data.ageInfo.value")
                            .type(JsonFieldType.STRING)
                            .description("연령대 정보 고유값 이름"),
                    fieldWithPath("data.ageInfo.description")
                            .type(JsonFieldType.STRING)
                            .description("연령대 정보 설명"),
                    fieldWithPath("data.hashTagInfo")
                            .type(JsonFieldType.ARRAY)
                            .description(
                                    "관심사 해시태그 목록 정보. 우리 서비스에서 사용되는 모든 해시태그 목록을 나열하되, 그 중 사용자가 관심사로 등록한 것과 등록하지 않은 것으로 구분합니다."),
                    fieldWithPath("data.hashTagInfo[].value")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 고유값 이름"),
                    fieldWithPath("data.hashTagInfo[].description")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 값 한글 설명"),
                    fieldWithPath("data.hashTagInfo[].isSelected")
                            .type(JsonFieldType.BOOLEAN)
                            .description("사용자가 해당 해시태그를 관심사로 등록했는지 여부"));

    private ResultActions getResultActions(MockHttpSession session) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.get("/api/users/info/simple").session(session));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "userSimpleInfo/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("간단한 회원 정보 조회 api")
                                .description("사용자 정보 수정 시 제공되는 기존 사용자 정보를 조회")
                                .responseFields(responseFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 사용자정보_간단_조회_성공() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        // when
        ResultActions resultActions = getResultActions(session);

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
