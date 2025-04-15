package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.user.dto.request.UpdateUserInfoRequest;
import com.ftm.server.application.port.out.s3.S3UserImageUploadPort;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;

public class UpdateUserInfoTest extends BaseTest {

    @MockitoSpyBean private S3UserImageUploadPort s3UserImageUploadPort;

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
                            .description("관심사 해시태그 목록 정보"),
                    fieldWithPath("data.hashTagInfo[].value")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 고유값 이름"),
                    fieldWithPath("data.hashTagInfo[].description")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 값 한글 설명"),
                    fieldWithPath("data.hashTagInfo[].isSelected")
                            .type(JsonFieldType.BOOLEAN)
                            .description("사용자가 해당 해시태그를 관심사로 등록했는지 여부"));

    List<RequestPartDescriptor> requestPartDescriptors =
            List.of(
                    partWithName("data")
                            .description("사용자 변경 정보")
                            .attributes(
                                    new Attributes.Attribute("content-type", "application/json")),
                    partWithName("imageFile")
                            .description("이미지를 등록/갱신하는 경우에만 첨부")
                            .attributes(new Attributes.Attribute("content-type", "image/*")));

    List<FieldDescriptor> requestPartFieldDescriptors =
            List.of(
                    fieldWithPath("nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 별명")
                            .optional(),
                    fieldWithPath("age")
                            .type(JsonFieldType.STRING)
                            .description("연령대 : 사용자 정보 옵션 조회 API에서 제공된 값으로 넣어주세요.")
                            .optional(),
                    fieldWithPath("imageAction")
                            .type(JsonFieldType.STRING)
                            .description("이미지 처리 방법")
                            .attributes(
                                    new Attributes.Attribute(
                                            "constraint",
                                            """
                            이미지 처리 방법 +
                            1. UPLOAD : 이미지 등록/변경 +
                            2. DELETE : 이미지를 삭제함
                            """))
                            .optional(),
                    fieldWithPath("hashtags")
                            .type(JsonFieldType.ARRAY)
                            .description("관심사 정보 : 사용자 정보 옵션 조회 API에서 제공된 값으로 넣어주세요.")
                            .optional());

    private ResultActions getResultActions(
            MockHttpSession session, MockMultipartFile image, MockMultipartFile data)
            throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.multipart("/api/users/info")
                        .file(image)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .session(session)
                        .with(
                                request -> {
                                    request.setMethod("PATCH"); // PATCH로 변경!
                                    return request;
                                }));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "userInfoUpdate/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestParts(requestPartDescriptors),
                requestPartFields("data", requestPartFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("사용자 정보 수정 api")
                                .description("사용자 정보를 수정함")
                                .responseFields(responseFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 사용자정보_수정_성공() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        UpdateUserInfoRequest request = new UpdateUserInfoRequest("거북이", null, null, "UPLOAD");
        MockMultipartFile image =
                new MockMultipartFile("imageFile", "test.jpg", "image/jpg", "test".getBytes());
        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "",
                        "application/json",
                        mapper.writeValueAsString(request).getBytes());

        // s3 실제 호출 대신 mock 대입
        doReturn("users/test.jpg").when(s3UserImageUploadPort).uploadImage(image);

        // when
        ResultActions resultActions = getResultActions(session, image, data);

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 사용자정보_수정_실패1() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        UpdateUserInfoRequest request = new UpdateUserInfoRequest("거북이", null, null, "UPLOAD");
        MockMultipartFile image =
                new MockMultipartFile("imageFile", "test.jpg", "plain/text", "test".getBytes());
        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "",
                        "application/json",
                        mapper.writeValueAsString(request).getBytes());

        // when
        ResultActions resultActions = getResultActions(session, image, data);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.INVALID_IMAGE_FORMAT.getHttpStatus().value()))
                .andExpect(
                        jsonPath("code").value(ErrorResponseCode.INVALID_IMAGE_FORMAT.getCode()));

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 사용자정보_수정_실패2() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        UpdateUserInfoRequest request = new UpdateUserInfoRequest("거북이", null, null, "UPLOAD");
        MockMultipartFile image =
                new MockMultipartFile("imageFile", "test.jpg", "image/jpg", "test".getBytes());
        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "",
                        "application/json",
                        mapper.writeValueAsString(request).getBytes());

        // s3 실제 호출 대신 mock 대입
        doThrow(new CustomException(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE))
                .when(s3UserImageUploadPort)
                .uploadImage(image);

        // when
        ResultActions resultActions = getResultActions(session, image, data);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE.getHttpStatus().value()))
                .andExpect(
                        jsonPath("code").value(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE.getCode()));

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
