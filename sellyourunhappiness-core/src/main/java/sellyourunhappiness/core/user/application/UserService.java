package sellyourunhappiness.core.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User saveUser(OAuthAttributes attributes, SocialType socialType) {
		User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
		return userRepository.save(createdUser);
	}

	public Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return userRepository.findBySocialTypeAndEmail(socialType, email);
	}

}