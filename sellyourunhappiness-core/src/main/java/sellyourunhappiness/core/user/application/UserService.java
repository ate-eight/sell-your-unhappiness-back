package sellyourunhappiness.core.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.infrastructure.UserRepository;


@RequiredArgsConstructor
@Service
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
			.role(Role.USER)
			.status(UserStatus.ACTIVE)
			.socialType(socialType)
			.build();
		return userRepository.save(createdUser);
	}

	public Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return userRepository.findBySocialTypeAndEmail(socialType, email);
	}

}