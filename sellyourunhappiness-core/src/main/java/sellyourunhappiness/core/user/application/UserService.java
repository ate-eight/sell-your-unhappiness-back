package sellyourunhappiness.core.user.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

import static sellyourunhappiness.core.user.domain.User.create;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.: " + email));
	}

	public User save(String name, String email, String profileUrl, SocialType socialType) {
		User createdUser = create(name, email, profileUrl, socialType);
		return userRepository.save(createdUser);
	}

	public User findBySocialTypeAndEmail(SocialType socialType, String email) {
		return userRepository.findBySocialTypeAndEmail(socialType, email)
				.orElseThrow(() -> new IllegalArgumentException("소셜 타입, 이메일과 일치하는 유저가 없습니다. SocialType : " + socialType + " email : " + email));
	}
}