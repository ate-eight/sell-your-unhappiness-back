package sellyourunhappiness.api.board_like.controller;


import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sellyourunhappiness.api.board_like.application.LikeBroker;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.slack.component.SlackComponent;


@WebMvcTest(LikeController.class)
@AutoConfigureRestDocs
@DisplayName("게시판 좋아요 CRUD API")
@WithMockUser(username = "MockLee", roles = {"USER"}) //각자 API마다 권한이 달라지면?
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeBroker likeBroker;

    @MockBean
    private JwtService jwtService;  //Service로 등록하냐 Component로 등록하냐 왜 이걸 빈으로 해야되냐?
    //운영 코드를 안건드는 선에서 해보면 좋을 듯 하다.

    @MockBean
    private SlackComponent slackComponent;

    @Test
    @DisplayName("게시글 좋아요 API")
    void createLike() throws Exception {
        //given
        String email = "test";
        Long boardId = 1L;
        BDDMockito.willDoNothing().given(likeBroker).createLike(email, boardId);

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/board/{boardId}/like", boardId)
                        .header("Authorization", "Bearer XXX")
                        .with(csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "board-like"
                                ,preprocessRequest(prettyPrint())
                                ,preprocessResponse(prettyPrint())
                                , ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Board API")
                                                .summary("게시글 좋아요")
                                                .requestHeaders(
                                                        HeaderDocumentation.headerWithName("Authorization").description("Basic auth credentials")
                                                )
                                                .pathParameters(
                                                        RequestDocumentation.parameterWithName("boardId").description(
                                                                "게시글 ID")
                                                )
                                                .requestSchema(null)
                                                .responseSchema(null)
                                                .build()
                                )
                        )
                );
    }


}