package sellyourunhappiness.api.user.application;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.user.dto.UserResponse;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBroker {
	private final UserService userService;
	private final JwtService jwtService;

	public UserResponse getUserByEmail(String email) {
		User user = userService.findByEmail(email);
		return new UserResponse(user.getName(), user.getEmail());
	}

	public Map<String, String> refreshAccessToken(String refreshToken) {
		return jwtService.refreshAccessToken(refreshToken);
	}

	public User getUserByOauth(String name, String email, String profileUrl, SocialType socialType) {
		try {
			return userService.findBySocialTypeAndEmail(socialType, email);
		} catch (IllegalArgumentException e) {
			log.debug(e.getMessage());
		}
		return userService.save(name, email, profileUrl, socialType);
	}
}
