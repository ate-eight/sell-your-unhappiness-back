package sellyourunhappiness.api.user.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.api.user.dto.UserResponse;

@ApiResponseAnnotation
@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserBroker userBroker;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/user")
	public ApiResponse getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		UserResponse response = userBroker.getUserByEmail(customOAuth2User.getEmail());

		return ApiResponse.create(response);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/v1/login/success")
	public ApiResponse handleAuthenticationSuccess(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		String email = customOAuth2User.getEmail();
		Map<String, String> tokens = userBroker.generateAndReturnTokens(email);

		return ApiResponse.create(tokens);
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/v1/user/token-refresh")
	public ApiResponse refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
		Map<String, String> tokens = userBroker.refreshAccessToken(refreshToken);

		return ApiResponse.create(tokens);
	}
}
