package sellyourunhappiness.api.user.application;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.application.JwtService;
import sellyourunhappiness.global.config.security.oauth2.OAuthAttributes;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;

@RequiredArgsConstructor
@Service
public class UserBroker {
	private final UserService userService;
	private final JwtService jwtService;

	public User getUserByEmail(String email) {
		return userService.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.: " + email));
	}

	public Map<String, String> refreshAccessToken(String refreshToken) {
		return jwtService.refreshAccessToken(refreshToken);
	}

	public User getUser(OAuthAttributes attributes, SocialType socialType) {
		return userService.findBySocialTypeAndEmail(socialType, attributes.getOauth2UserInfo().getEmail())
			.orElseGet(() -> {
				String name = attributes.getOauth2UserInfo().getNickname();
				String email = attributes.getOauth2UserInfo().getEmail();
				String profileUrl = attributes.getOauth2UserInfo().getImageUrl();

				return userService.saveUser(name, email, profileUrl, socialType);
			});
	}
}
