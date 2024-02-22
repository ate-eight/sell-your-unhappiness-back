package sellyourunhappiness.api.board.controller;


import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import sellyourunhappiness.api.board.application.BoardBroker;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board.dto.BoardResponse;
import sellyourunhappiness.api.board.dto.BoardSearchCondition;
import sellyourunhappiness.api.board.dto.BoardSearchResponse;
import sellyourunhappiness.api.config.enums.EnumBean;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.page.PageResponse;
import sellyourunhappiness.api.config.page.PageValue;
import sellyourunhappiness.api.config.slack.component.SlackComponent;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


import org.springframework.context.annotation.Import;

import sellyourunhappiness.api.config.MvcTestConfig;


@WebMvcTest(BoardController.class)
@AutoConfigureRestDocs
@Import(MvcTestConfig.class)
@DisplayName("게시판 CRUD API")
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardBroker boardBroker;

    @MockBean
    private EnumBean enumBean;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private SlackComponent slackComponent;
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
                                                fieldWithPath("common").type(JsonFieldType.OBJECT),
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("HTTP 상태"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                                                fieldWithPath("success").type(JsonFieldType.STRING).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.STRING),
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
                                                fieldWithPath("type").type(JsonFieldType.STRING).description("게시글 타입"),
                                                fieldWithPath("status").type(JsonFieldType.STRING).description("게시글 상태"),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                                fieldWithPath("createTime").type(JsonFieldType.STRING).description("등록일자"),
                                                fieldWithPath("modifiedTime").type(JsonFieldType.STRING).description("수정일자"))
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
                                                fieldWithPath("pageValue").type(JsonFieldType.OBJECT).description(""),
                                                fieldWithPath("pageValue.totalPage").type(JsonFieldType.NUMBER).description("총 페이지"),
                                                fieldWithPath("pageValue.pageSize").type(JsonFieldType.NUMBER).description("페이지 출력수"),
                                                fieldWithPath("pageValue.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                                fieldWithPath("pageValue.totalElements").type(JsonFieldType.NUMBER).description("총 게시글"),
                                                fieldWithPath("pageValue.num").type(JsonFieldType.NUMBER).description("현재 페이지 최상단 게시글 번호(미사용)"),
                                                fieldWithPath("pageValue.block").type(JsonFieldType.ARRAY).description("하단 페이지 블럭(미사용)"),
                                                fieldWithPath("pageValue.previous").type(JsonFieldType.NUMBER).description("10페이지 이전(미사용)").optional(),
                                                fieldWithPath("pageValue.after").type(JsonFieldType.NUMBER).description("10페이지 다음(미사용)").optional(),
                                                fieldWithPath("contents").type(JsonFieldType.ARRAY).description(""),
                                                fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                                                fieldWithPath("contents[].type").type(JsonFieldType.STRING).description("게시글 타입"),
                                                fieldWithPath("contents[].status").type(JsonFieldType.STRING).description("게시글 상태"),
                                                fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                                fieldWithPath("contents[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                                fieldWithPath("contents[].createTime").type(JsonFieldType.STRING).description("등록일자"))
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
                )
                .andExpect(status().isOk())
                .andDo(
                        document("board-types-get",
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("Board API")
                                        .summary("게시글 타입 조회")
                                        .responseFields(
                                                fieldWithPath("types").type(JsonFieldType.ARRAY).description(""))
                                        .responseSchema(null)
                                        .build())));
    }
}