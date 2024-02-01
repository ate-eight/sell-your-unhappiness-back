// package sellyourunhappiness.api.user.controller;
//
// import static com.epages.restdocs.apispec.ResourceDocumentation.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
// import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.Collections;
// import java.util.Map;
// import java.util.Optional;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.FilterType;
// import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
// import org.springframework.http.MediaType;
// import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
// import org.springframework.restdocs.payload.JsonFieldType;
// import org.springframework.security.authentication.TestingAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.epages.restdocs.apispec.ResourceSnippetParameters;
// import com.epages.restdocs.apispec.Schema;
//
// import sellyourunhappiness.api.user.application.UserBroker;
// import sellyourunhappiness.global.config.security.oauth.CustomOAuth2User;
// import sellyourunhappiness.core.user.domain.User;
// import sellyourunhappiness.core.user.domain.enums.Role;
// import sellyourunhappiness.core.user.domain.enums.SocialType;
// import sellyourunhappiness.core.user.domain.enums.UserStatus;
// import sellyourunhappiness.global.config.jwt.JwtTokenFilter;
// import sellyourunhappiness.global.exception.ErrorDetectAdvisor;
//
//
// @MockBean(JpaMetamodelMappingContext.class)
// @WebMvcTest(value = UserController.class, excludeFilters = {
// 	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ErrorDetectAdvisor.class, JwtTokenFilter.class})
// })
// @AutoConfigureRestDocs
// @WebAppConfiguration
// public class UserControllerTest {
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockBean
// 	private CustomOAuth2User customOAuth2User;
//
// 	@MockBean
// 	private UserBroker userBroker;
//
// 	@DisplayName("사용자 정보 조회 API 테스트")
// 	@Test
// 	public void shouldGetUserInfo() throws Exception {
// 		// Given
// 		String email = "dbscks9793@gmail.com";
// 		User mockUser = new User("박윤찬", email, null, Role.USER, UserStatus.ACTIVE, SocialType.GOOGLE);
// 		when(userBroker.getUserByEmail(anyString())).thenReturn(mockUser);
// 		// CustomOAuth2User 설정
// 		CustomOAuth2User customOAuth2User = new CustomOAuth2User(
// 			Collections.singleton(new SimpleGrantedAuthority(mockUser.getRole().getKey())),
// 			Collections.singletonMap("email", email),
// 			"email",
// 			email,
// 			mockUser.getRole()
// 		);
// 		TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(customOAuth2User, null, "ROLE_USER");
// 		testingAuthenticationToken.setAuthenticated(true);
// 		SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
//
// 		// When & Then
// 		mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/user/info")
// 				.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.data.email").value(email))
// 			.andExpect(jsonPath("$.data.username").value("박윤찬"))
// 			.andDo(document("user-info",
// 				preprocessRequest(prettyPrint()),
// 				preprocessResponse(prettyPrint()),
// 				resource(ResourceSnippetParameters.builder()
// 					.tag("User API")
// 					.summary("사용자 정보 조회 API")
// 					.responseFields(
// 						fieldWithPath("data.username").type(JsonFieldType.STRING).description("사용자 이름"),
// 						fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
// 						fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드"),
// 						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
// 					)
// 					.responseSchema(Schema.schema("User Info API Response"))
// 					.build())
// 			));
//
// 		SecurityContextHolder.clearContext();
// 	}
//
// 	@DisplayName("액세스 토큰 갱신 API 테스트")
// 	@Test
// 	public void shouldRefreshAccessToken() throws Exception {
// 		// Given
// 		String refreshToken = "mockRefreshToken";
// 		String newAccessToken = "newMockAccessToken";
// 		Map<String, String> tokens = Map.of("accessToken", newAccessToken);
// 		when(userBroker.refreshAccessToken(refreshToken)).thenReturn(tokens);
//
// 		TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(customOAuth2User, null,
// 			"ROLE_USER");
// 		testingAuthenticationToken.setAuthenticated(true);
// 		SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
//
// 		// When & Then
// 		mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/user/refresh")
// 				.with(csrf())
// 				.with(authentication(testingAuthenticationToken)) // 수정된 부분
// 				.header("Refresh-Token", refreshToken)
// 				.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.data.accessToken").value(newAccessToken))
// 			.andDo(document("refresh-token",
// 				preprocessRequest(prettyPrint()),
// 				preprocessResponse(prettyPrint()),
// 				resource(ResourceSnippetParameters.builder()
// 					.tag("User API")
// 					.summary("액세스 토큰 갱신 API")
// 					.requestHeaders(
// 						headerWithName("Refresh-Token").description("리프레시 토큰")
// 					)
// 					.responseFields(
// 						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("새로 발급된 액세스 토큰"),
// 						fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드"),
// 						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
// 					)
// 					.responseSchema(Schema.schema("Refresh Token API Response"))
// 					.build())
// 			));
//
// 		// Verify
// 		verify(userBroker, times(1)).refreshAccessToken(refreshToken);
// 	}
//
// }

package sellyourunhappiness.api.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import net.gpedro.integrations.slack.SlackApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.global.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.core.user.application.JwtService;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

@WebMvcTest(value = UserController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserBroker userBroker;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SlackApi slackApi;

	@DisplayName("사용자 정보 조회 API 테스트")
	@Test
	public void shouldGetUserInfo() throws Exception {
		// Given
		String email = "dbscks9793@gmail.com";
		User mockUser = new User("박윤찬", email, null, Role.USER, UserStatus.ACTIVE, SocialType.GOOGLE);
		when(userBroker.getUserByEmail(anyString())).thenReturn(mockUser);
		// CustomOAuth2User 설정
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(mockUser.getRole().getKey())),
			Collections.singletonMap("email", email),
			"email",
			email,
			mockUser.getRole()
		);
		TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(customOAuth2User, null, "ROLE_USER");
		testingAuthenticationToken.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

		// When & Then
		mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/user/info")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.email").value(email))
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

}




