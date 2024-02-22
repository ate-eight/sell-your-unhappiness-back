package sellyourunhappiness.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.security.handler.CustomOAuth2AccessDeniedHandler;
import sellyourunhappiness.api.config.security.handler.CustomOAuth2LoginFailureHandler;
import sellyourunhappiness.api.config.security.handler.CustomOAuth2LoginSuccessHandler;
import sellyourunhappiness.api.config.security.oauth2.application.CustomOAuth2UserService;


@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
	private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;
	private final CustomOAuth2AccessDeniedHandler customOAuth2AccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.headers((headerConfig) ->
				headerConfig.frameOptions(FrameOptionsConfig::disable)
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
			})
			.exceptionHandling(exceptionHandling ->
				exceptionHandling.accessDeniedHandler(customOAuth2AccessDeniedHandler)
			);
		return http.build();
	}
}