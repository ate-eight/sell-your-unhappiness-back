package sellyourunhappiness.api.board.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

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

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;

import sellyourunhappiness.api.board.application.BoardBroker;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board.dto.BoardResponse;
import sellyourunhappiness.api.board.dto.BoardSearchCondition;
import sellyourunhappiness.api.board.dto.BoardSearchResponse;
import sellyourunhappiness.api.config.MvcTestConfig;
import sellyourunhappiness.api.config.enums.EnumBean;
import sellyourunhappiness.api.config.page.PageResponse;
import sellyourunhappiness.api.config.page.PageValue;
import sellyourunhappiness.api.config.response.aspect.ApiResponseAspect;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;

@EnableAspectJAutoProxy
@AutoConfigureRestDocs
@Import({MvcTestConfig.class, ApiResponseAspect.class})
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardBroker boardBroker;

    @MockBean
    private EnumBean enumBean;

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
        BoardRegisterParam param = new BoardRegisterParam("회사", "회사에 대한 고민", "회사 고민 테스트 내용");

        given(boardBroker.save(any(BoardRegisterParam.class))).willReturn("Success");

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/board")
                    .content(objectMapper.writeValueAsString(param))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andDo(
                document("board-post",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Board API")
                        .summary("게시글 저장")
                        .requestFields(
                            fieldWithPath("type").type(JsonFieldType.STRING).description("게시글 타입"),
                            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"))
                        .responseFields(
                            fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 객체"),
                            fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                            fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                            fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터 객체"),
                            fieldWithPath("data.message").type(JsonFieldType.STRING).description("성공 또는 실패"))
                        .requestSchema(null)
                        .responseSchema(null)
                        .build())));
    }

    @Test
    @DisplayName("게시글 단건 조회 API")
    void getBoardById() throws Exception {
        //given
        Long id = 1L;

        BoardResponse response = BoardResponse.builder()
            .type("회사")
            .status("판매중")
            .title("회사에 대한 고민")
            .content("회사 고민 테스트 내용")
            .createTime(LocalDateTime.parse("2024-01-19T13:30:45"))
            .modifiedTime(LocalDateTime.parse("2024-01-19T13:30:45"))
            .build();

        given(boardBroker.findById(any(Long.class))).willReturn(response);

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/board/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andDo(
                document("board-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Board API")
                        .summary("게시글 단건 조회")
                        .pathParameters(
                            parameterWithName("id").type(SimpleType.NUMBER).description("게시글 id"))
                        .responseFields(
                            fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 객체"),
                            fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                            fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                            fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터 객체"),
                            fieldWithPath("data.type").type(JsonFieldType.STRING).description("게시글 타입"),
                            fieldWithPath("data.status").type(JsonFieldType.STRING).description("게시글 상태"),
                            fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                            fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                            fieldWithPath("data.createTime").type(JsonFieldType.STRING).description("등록일자"),
                            fieldWithPath("data.modifiedTime").type(JsonFieldType.STRING).description("수정일자"))
                        .responseSchema(null)
                        .build())));
    }

    @Test
    @DisplayName("게시글 전체 조회 API")
    void searchBoards() throws Exception {
        //given
        BoardSearchCondition condition = new BoardSearchCondition("회사", "판매중", 1);

        PageValue pageValue = new PageValue(1, 10, 0, 3);

        List<BoardSearchResponse> boardSearchResponses = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            BoardSearchResponse boardSearchResponse = BoardSearchResponse.builder()
                .id((long) i)
                .type("회사" + i)
                .status("판매중" + i)
                .title("제목" + i)
                .content("내용" + i)
                .createTime(LocalDateTime.parse("2024-01-19T13:30:45"))
                .build();

            boardSearchResponses.add(boardSearchResponse);
        }

        given(boardBroker.findByTypeAndStatusAllDesc(any(BoardSearchCondition.class)))
            .willReturn(new PageResponse<>(pageValue, boardSearchResponses));

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/boards")
                    .queryParam("type", "회사")
                    .queryParam("status", "판매중")
                    .queryParam("page", "1")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andDo(
                document("boards-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Board API")
                        .summary("게시글 전체 조회")
                        .queryParameters(
                            parameterWithName("type").type(SimpleType.STRING).description("게시글 타입"),
                            parameterWithName("status").type(SimpleType.STRING).description("게시글 상태"),
                            parameterWithName("page").type(SimpleType.NUMBER).description("페이지 번호"))
                        .responseFields(
                            fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 객체"),
                            fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                            fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                            fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터 객체"),
                            fieldWithPath("data.pageValue").type(JsonFieldType.OBJECT).description(""),
                            fieldWithPath("data.pageValue.totalPage").type(JsonFieldType.NUMBER).description("총 페이지"),
                            fieldWithPath("data.pageValue.pageSize").type(JsonFieldType.NUMBER).description("페이지 출력수"),
                            fieldWithPath("data.pageValue.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                            fieldWithPath("data.pageValue.totalElements").type(JsonFieldType.NUMBER).description("총 게시글"),
                            fieldWithPath("data.pageValue.num").type(JsonFieldType.NUMBER).description("현재 페이지 최상단 게시글 번호(미사용)"),
                            fieldWithPath("data.pageValue.block").type(JsonFieldType.ARRAY).description("하단 페이지 블럭(미사용)"),
                            fieldWithPath("data.pageValue.previous").type(JsonFieldType.NUMBER).description("10페이지 이전(미사용)").optional(),
                            fieldWithPath("data.pageValue.after").type(JsonFieldType.NUMBER).description("10페이지 다음(미사용)").optional(),
                            fieldWithPath("data.contents").type(JsonFieldType.ARRAY).description(""),
                            fieldWithPath("data.contents[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                            fieldWithPath("data.contents[].type").type(JsonFieldType.STRING).description("게시글 타입"),
                            fieldWithPath("data.contents[].status").type(JsonFieldType.STRING).description("게시글 상태"),
                            fieldWithPath("data.contents[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                            fieldWithPath("data.contents[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                            fieldWithPath("data.contents[].createTime").type(JsonFieldType.STRING).description("등록일자"))
                        .responseSchema(null)
                        .build())));
    }

    @Test
    @DisplayName("게시글 타입 조회 API")
    void getBoardTypes() throws Exception {
        //given
        List<String> response = EnumSet.allOf(BoardType.class).stream()
            .map(BoardType::getName).toList();

        given(enumBean.get(any(String.class))).willReturn(response);

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/board/types")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andDo(
                document("board-types-get",
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Board API")
                        .summary("게시글 타입 조회")
                        .responseFields(
                            fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 객체"),
                            fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태"),
                            fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                            fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터 객체"),
                            fieldWithPath("data.types").type(JsonFieldType.ARRAY).description(""))
                        .responseSchema(null)
                        .build())));
    }
}