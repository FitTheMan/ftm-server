package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.out.persistence.post.SavePostPort;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadUserPickLatestPostsTest extends BaseTest {

    @Autowired private SavePostPort savePostPort;

    private final List<ParameterDescriptor> queryParameters =
            List.of(
                    parameterWithName("limit")
                            .description("한번 로딩 시 불러올 게시글의 개수")
                            .attributes(new Attributes.Attribute("constraint", "Integer")),
                    parameterWithName("lastCursor")
                            .optional()
                            .description("이전 로딩의 마지막 커서 시점 입력")
                            .attributes(
                                    new Attributes.Attribute(
                                            "constraint",
                                            "YYYY-MM-DDTHH:mm:SS.msss , 이전 스크롤 로딩 응답값(nextCursorDateTime) 필드 그대로 사용. 첫번째 스크롤인 경우 전달 X")));

    private final List<FieldDescriptor> responseFields =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.data")
                            .type(ARRAY)
                            .optional()
                            .description("응답 데이터가 없으면 빈 배열"),
                    fieldWithPath("data.data[].postId").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("data.data[].title").type(STRING).description("게시글 제목"),
                    fieldWithPath("data.data[].authorId").type(NUMBER).description("작성자 user ID"),
                    fieldWithPath("data.data[].authorName").type(STRING).description("작성자 이름"),
                    fieldWithPath("data.data[].viewCount").type(NUMBER).description("조회수"),
                    fieldWithPath("data.data[].likeCount").type(NUMBER).description("좋아요 수"),
                    fieldWithPath("data.data[].scrapCount").type(NUMBER).description("스크랩 수"),
                    fieldWithPath("data.data[].imageUrl").type(STRING).description("이미지 url"),
                    fieldWithPath("data.data[].hashtags")
                            .type(ARRAY)
                            .description("게시글 해시태그 : 한글 태그 표시. 없는 경우 빈 배열([])로 표시"),
                    fieldWithPath("data.data[].userBookmarkYn")
                            .type(BOOLEAN)
                            .description("사용자 북마크 등록 여부"),
                    fieldWithPath("data.nextCursorDateTime")
                            .type(STRING)
                            .optional()
                            .description("다음 스크롤 로딩 요청 시, 요청 파라미터 lastCursor의 값이 됨")
                            .attributes(
                                    new Attributes.Attribute(
                                            "nullable", "hasNext가 false인 경우 null")),
                    fieldWithPath("data.hasNext")
                            .type(BOOLEAN)
                            .description("불러올 게시글이 추가로 남았는지 여부"));

    private ResultActions getResultActions() throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/posts/userpick/all/latest")
                        .queryParam("limit", "5"));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadUserPickAllLatest/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFields),
                queryParameters(queryParameters),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("\"그루밍 이야기\" 최신순 조회 api")
                                .description("그루밍 라운지 내 \"그루밍 이야기\" 최신순 목록 조회 api 입니다.")
                                .responseFields(responseFields)
                                .build()));
    }

    @Test
    @Transactional
    @DisplayName("테스트 성공")
    public void test1() throws Exception {
        // given

        SessionAndUser sessionAndUser = createUserAndLoginAndReturnUser(); // 로그인 처리

        User user = sessionAndUser.user();

        // test 용 post 생성
        savePostPort.savePost(
                Post.create(
                        SavePostCommand.from(
                                user.getId(),
                                new SavePostRequest(
                                        "test1",
                                        List.of(PostHashtag.SUN_CARE, PostHashtag.CLEANSING),
                                        "content1",
                                        new ArrayList<>()),
                                new ArrayList<>(),
                                new ArrayList<>())));

        savePostPort.savePost(
                Post.create(
                        SavePostCommand.from(
                                user.getId(),
                                new SavePostRequest(
                                        "test2",
                                        List.of(
                                                PostHashtag.BOTTOM_CLOTHING,
                                                PostHashtag.FASHION_ACCESSORIES),
                                        "content2",
                                        new ArrayList<>()),
                                new ArrayList<>(),
                                new ArrayList<>())));

        // when
        ResultActions resultActions = getResultActions();

        // then
        resultActions
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.data", hasSize(2)));

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
