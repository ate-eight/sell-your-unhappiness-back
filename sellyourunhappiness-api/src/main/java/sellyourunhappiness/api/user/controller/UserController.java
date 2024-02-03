package sellyourunhappiness.api.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.response.BaseResponse;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.api.user.dto.UserResponse;


@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserBroker userBroker;

	@GetMapping("/v1/user")
	public BaseResponse<UserResponse> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		UserResponse response = userBroker.getUserByEmail(customOAuth2User.getEmail());
		return new BaseResponse<>(response, HttpStatus.OK, "유저 조회 성공");
	}

	@PostMapping("/v1/user/token-refresh")
	public BaseResponse<Map<String, String>> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
		try {
			Map<String, String> tokens = userBroker.refreshAccessToken(refreshToken);
			return new BaseResponse<>(tokens, HttpStatus.OK, "토큰 갱신 성공");
		} catch (RuntimeException ex) {
			return new BaseResponse<>(null, HttpStatus.UNAUTHORIZED, "인증 실패: " + ex.getMessage());
		}
	}
}
