package sellyourunhappiness.api.board_like.controller;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board_like.application.BoardLikeBroker;

import sellyourunhappiness.api.board_like.dto.BoardLikeParam;

import sellyourunhappiness.api.config.enums.EnumBean;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.response.aspect.ApiResponseAspect;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.config.slack.component.SlackComponent;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;


@EnableAspectJAutoProxy
@Import(ApiResponseAspect.class)
@WebMvcTest(BoardLikeController.class)
@AutoConfigureRestDocs
@DisplayName("게시판 좋아요 CRUD API")
//@WithMockUser(username = "MockLee", roles = {"USER"}) //각자 API마다 권한이 달라지면?

class BoardLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardLikeBroker likeBroker;

    @MockBean
    private EnumBean enumBean;


    @Autowired
    private ApiResponseAspect aspect;


    @MockBean
    private JwtService jwtService;  //Service로 등록하냐 Component로 등록하냐 왜 이걸 빈으로 해야되냐?
    //운영 코드를 안건드는 선에서 해보면 좋을 듯 하다.

    @MockBean
    private SlackComponent slackComponent;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // CustomOAuth2User 설정
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
    @DisplayName("게시글 좋아요 API")
    void like() throws Exception {
        //given
        Long boardId = 1L;


        BDDMockito.given(likeBroker.like(any(String.class), any(Long.class),any(String.class))).willReturn("success");
        //when, then

        BoardLikeParam param = new BoardLikeParam("좋아요");

        String type = "like";
        BDDMockito.given(likeBroker.like(any(String.class), any(Long.class),any(String.class))).willReturn("success");
        //when, then


        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/board/{boardId}/like", boardId)
                        .header("Authorization", "Bearer XXX")
                        .content(objectMapper.writeValueAsString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
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
                                                .tag("Like API")
                                                .summary("게시글 좋아요")
                                                .requestHeaders(
                                                        HeaderDocumentation.headerWithName("Authorization").description("Basic auth credentials")
                                                )
                                                .pathParameters(
                                                        RequestDocumentation.parameterWithName("boardId").description(
                                                                "게시글 ID")
                                                )
                                                .responseFields(
                                                        fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 정보"),
                                                        fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                                        fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                                                        fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                                        fieldWithPath("data.message").type(JsonFieldType.STRING).description("데이터 메시지")
                                                )
                                                .requestFields(
                                                        fieldWithPath("type").type(JsonFieldType.STRING).description("좋아요 타입")
                                                )
                                                .requestSchema(null)
                                                .responseSchema(null)
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("좋아요 타입 조회 API")
    void getLikeTypes() throws Exception {
        //given
        List<String> response = EnumSet.allOf(LikeType.class).stream()
                .map(LikeType::getName).toList();

        given(enumBean.get(any(String.class))).willReturn(response);

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/like/types")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("like-types-get",
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("Like API")
                                        .summary("좋아요 타입 조회")
                                        .responseFields(

                                                fieldWithPath("common").type(JsonFieldType.OBJECT).description("공통 응답 정보"),
                                                fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                                fieldWithPath("common.message").type(JsonFieldType.STRING).description("에러 메시지"),
                                                fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                                fieldWithPath("data.types").type(JsonFieldType.ARRAY).description("타입")
                                        )
                                        .responseSchema(null)
                                        .build())));
    }


}
