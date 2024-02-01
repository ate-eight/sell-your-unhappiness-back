package sellyourunhappiness.core.user.application;

import static sellyourunhappiness.core.user.domain.enums.SocialType.*;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.security.CustomOAuth2User;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String Accesstoken = userRequest.getAccessToken().getTokenValue();
		System.out.println("Accesstoken = " + Accesstoken);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, oAuth2User.getAttributes());
		Map<String, Object> attributes = oAuth2User.getAttributes();

		User createdUser = getUser(extractAttributes, socialType);

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
			attributes,
			extractAttributes.getNameAttributeKey(),
			createdUser.getEmail(),
			createdUser.getRole()
		);
	}

	private SocialType getSocialType(String registrationId) {
		if ("google".equals(registrationId)) {
			return GOOGLE;
		}
		throw new IllegalArgumentException("Unknown SocialType: " + registrationId);
	}


	private User getUser(OAuthAttributes attributes, SocialType socialType) {
		User findUser = userRepository.findBySocialTypeAndEmail(socialType,
			attributes.getOauth2UserInfo().getEmail()).orElse(null);

		if(findUser == null) {
			return saveUser(attributes, socialType);
		}
		return findUser;
	}

	private User saveUser(OAuthAttributes attributes, SocialType socialType) {
		User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
		return userRepository.save(createdUser);
	}
}