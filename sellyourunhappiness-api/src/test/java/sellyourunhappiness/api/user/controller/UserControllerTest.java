package sellyourunhappiness.api.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.core.security.CustomOAuth2User;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.infrastructure.UserRepository;
import sellyourunhappiness.global.exception.ErrorDetectAdvisor;


@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(value = UserController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = ErrorDetectAdvisor.class))
@AutoConfigureRestDocs
@WebAppConfiguration
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;


	@MockBean
	private UserBroker userBroker;

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