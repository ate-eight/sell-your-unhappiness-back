package sellyourunhappiness.api.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.config.slack.component.SlackComponent;
import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;

@WebMvcTest(value = UserController.class)
@AutoConfigureRestDocs
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserBroker userBroker;

	@MockBean
	private JwtService jwtService;
	@MockBean
	private SlackComponent slackComponent;

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

		when(userBroker.getUserByEmail(anyString())).thenReturn(mockUserResponse);
	}

	@DisplayName("사용자 정보 조회 API 테스트")
	@Test
	public void shouldGetUserInfo() throws Exception {
		// When & Then
		mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/user")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.email").value("dbscks9793@gmail.com"))
			.andExpect(jsonPath("$.data.username").value("박윤찬"))
			.andDo(document("user-info",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				resource(ResourceSnippetParameters.builder()
					.tag("User API")
					.summary("사용자 정보 조회 API")
					.responseFields(
						fieldWithPath("data.username").type(JsonFieldType.STRING).description("사용자 이름"),
						fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
						fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
					)
					.responseSchema(Schema.schema("User Info API Response"))
					.build())
			));

		SecurityContextHolder.clearContext();
	}

	@DisplayName("액세스 토큰 갱신 API 테스트")
	@Test
	public void shouldRefreshAccessToken() throws Exception {
		// Given
		String refreshToken = "mockRefreshToken";
		String newAccessToken = "newMockAccessToken";
		when(userBroker.refreshAccessToken(refreshToken)).thenReturn(Map.of("accessToken", "newMockAccessToken"));

		// When & Then
		mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/user/token-refresh")
				.with(csrf())
				.header("Refresh-Token", refreshToken)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").value(newAccessToken))
			.andDo(document("refresh-token",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				resource(ResourceSnippetParameters.builder()
					.tag("User API")
					.summary("액세스 토큰 갱신 API")
					.requestHeaders(
						headerWithName("Refresh-Token").description("리프레시 토큰")
					)
					.responseFields(
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("새로 발급된 액세스 토큰"),
						fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
					)
					.responseSchema(Schema.schema("Refresh Token API Response"))
					.build())
			));
	}
}