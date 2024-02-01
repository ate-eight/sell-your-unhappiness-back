package sellyourunhappiness.core.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User saveUser(String name, String email, String profileUrl, SocialType socialType) {
		User createdUser = User.builder()
			.name(name)
			.email(email)
			.profileURL(profileUrl)
			.role(Role.GUEST) // 예시로 GUEST를 사용했습니다. 실제로는 적절한 롤을 설정해야 합니다.
			.status(UserStatus.ACTIVE) // 예시로 ACTIVE를 사용했습니다. 실제 상황에 맞게 조정 필요.
			.socialType(socialType)
			.build();
		return userRepository.save(createdUser);
	}

	public Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return userRepository.findBySocialTypeAndEmail(socialType, email);
	}

}