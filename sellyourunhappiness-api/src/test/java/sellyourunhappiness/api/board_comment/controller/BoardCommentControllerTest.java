package sellyourunhappiness.api.board_comment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import sellyourunhappiness.api.board_comment.application.BoardCommentBroker;
import sellyourunhappiness.api.board_comment.dto.BoardCommentRegisterParam;
import sellyourunhappiness.api.board_comment.dto.BoardCommentResponse;
import sellyourunhappiness.api.config.MvcTestConfig;
import sellyourunhappiness.api.config.response.aspect.ApiResponseAspect;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAspectJAutoProxy
@AutoConfigureRestDocs
@Import({MvcTestConfig.class, ApiResponseAspect.class})
@WebMvcTest(BoardCommentController.class)
class BoardCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardCommentBroker boardCommentBroker;

    @BeforeEach
    void setUp() {
        String email = "dbscks9793@gmail.com";
        User mockUser = new User("박윤찬", email, null, Role.USER, UserStatus.ACTIVE, SocialType.GOOGLE);
        UserResponse mockUserResponse = new UserResponse(mockUser.getName(), mockUser.getEmail());

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(mockUser.getRole().getKey())),
                Collections.singletonMap("email", email),
                "email",
                email,
                mockUser.getRole()
        );

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(customOAuth2User, null,
                "ROLE_USER");
        testingAuthenticationToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    @Test
    @DisplayName("게시글 저장 API")
    void register() throws Exception {
        //given
        BoardCommentRegisterParam param = new BoardCommentRegisterParam(1L, 1L, "테스트 댓글 내용");

        given(boardCommentBroker.save(any(BoardCommentRegisterParam.class))).willReturn("Success");

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/board-comment")
                                .content(objectMapper.writeValueAsString(param))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("board-comment-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("BoardComment API")
                                        .summary("댓글 저장")
                                        .requestFields(
                                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 댓글 ID"),
                                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"))
                                        .responseFields(
                                                fieldWithPath("common").type(JsonFieldType.OBJECT),
                                                fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                                                fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                                                fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.OBJECT),
                                                fieldWithPath("data.message").type(JsonFieldType.STRING).description("성공 또는 실패"))
                                        .requestSchema(null)
                                        .responseSchema(null)
                                        .build())));
    }

    @Test
    @DisplayName("게시글 전체 조회 API")
    void searchBoards() throws Exception {
        //given

        List<BoardCommentResponse> boardCommentResponses = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            BoardCommentResponse boardCommentResponse = BoardCommentResponse.builder()
                    .id((long) i)
                    .parentId(1L)
                    .content("내용" + i)
                    .createTime(LocalDateTime.parse("2024-01-19T13:30:45"))
                    .build();

            boardCommentResponses.add(boardCommentResponse);
        }

        given(boardCommentBroker.findAllByBoardId(any()))
                .willReturn(boardCommentResponses);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/board-comments")
                                .queryParam("boardId", "1")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("board-comments-get",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("BoardComment API")
                                        .summary("댓글 전체 조회")
                                        .queryParameters(
                                                parameterWithName("boardId").type(SimpleType.NUMBER).description("게시글 아이디"))
                                        .responseFields(
                                                fieldWithPath("common").type(JsonFieldType.OBJECT),
                                                fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                                                fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                                                fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.OBJECT),
                                                fieldWithPath("data.contents").type(JsonFieldType.ARRAY),
                                                fieldWithPath("data.contents[].id").type(JsonFieldType.NUMBER).description(""),
                                                fieldWithPath("data.contents[].parentId").type(JsonFieldType.NUMBER).description(""),
                                                fieldWithPath("data.contents[].content").type(JsonFieldType.STRING).description(""),
                                                fieldWithPath("data.contents[].createTime").type(JsonFieldType.STRING).description(""))
                                        .responseSchema(null)
                                        .build())));
    }
}