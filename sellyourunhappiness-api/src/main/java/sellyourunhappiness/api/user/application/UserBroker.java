package sellyourunhappiness.api.user.application;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.application.JwtService;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class UserBroker {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
	}
	public Map<String, String> refreshAccessToken(String refreshToken) {
		return jwtService.refreshAccessToken(refreshToken);
	}
}
