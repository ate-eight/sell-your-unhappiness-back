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
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import sellyourunhappiness.api.config.MvcTestConfig;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.user.domain.enums.Role;

@AutoConfigureRestDocs
@Import(MvcTestConfig.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserBroker userBroker;

	private void setupMockSecurityContextWithRole(String email, Role role) {
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role.getKey()));
		Map<String, Object> attributes = Collections.singletonMap("email", email);
		TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(
			new CustomOAuth2User(authorities, attributes, "email", email, role), null, authorities);

		authenticationToken.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		when(userBroker.getUserByEmail(email)).thenReturn(new UserResponse("박윤찬", email));
	}

	@DisplayName("사용자 정보 조회 API 테스트")
	@Test
	public void shouldGetUserInfo() throws Exception {
		// Given
		setupMockSecurityContextWithRole("dbscks9793@gmail.com", Role.USER);

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
						fieldWithPath("common.message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
						fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부")
					)
					.responseSchema(null).build())));

		SecurityContextHolder.clearContext();
	}


	@DisplayName("액세스 토큰 갱신 API 테스트")
	@Test
	public void shouldRefreshAccessToken() throws Exception {
		// Given
		setupMockSecurityContextWithRole("dbscks9793@gmail.com", Role.USER);
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
						fieldWithPath("common.message").type(JsonFieldType.STRING).optional().description("응답 메시지 (선택 사항)"),
						fieldWithPath("common.code").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("common.success").type(JsonFieldType.BOOLEAN).description("성공 여부")
					)
					.responseSchema(null).build())));

		SecurityContextHolder.clearContext();
	}
}
