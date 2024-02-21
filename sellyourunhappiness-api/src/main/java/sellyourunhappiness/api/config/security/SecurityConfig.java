package sellyourunhappiness.api.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.security.oauth2.application.CustomOAuth2UserService;
import sellyourunhappiness.api.config.security.handler.CustomOAuth2LoginFailureHandler;
import sellyourunhappiness.api.config.security.handler.CustomOAuth2LoginSuccessHandler;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
	private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.headers((headerConfig) ->
				headerConfig.frameOptions(FrameOptionsConfig::disable)
			)
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					//.requestMatchers(PathRequest.toH2Console()).permitAll()
					 // 왜 requestMatcher로 permiall을 해줬냐
					.anyRequest().permitAll()
			)

			.logout((logoutConfig) ->
				logoutConfig.logoutSuccessUrl("/")
			)
			.oauth2Login(oauth2Login -> {
				oauth2Login
					.userInfoEndpoint(userInfoEndpointConfig ->
						userInfoEndpointConfig.userService(customOAuth2UserService)
					)
					.successHandler(customOAuth2LoginSuccessHandler)
					.failureHandler(customOAuth2LoginFailureHandler);
			});
		return http.build();
	}
}
