package sellyourunhappiness.global.config.security.dto;

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
import sellyourunhappiness.api.user.application.UserBroker;
import sellyourunhappiness.global.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.global.config.security.oauth2.OAuthAttributes;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserBroker userBroker;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
		OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName,
			oAuth2User.getAttributes());
		Map<String, Object> attributes = oAuth2User.getAttributes();

		User createdUser = userBroker.getUser(extractAttributes, socialType);

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
		throw new IllegalArgumentException("SocialType이 일치하지 않습니다.: " + registrationId);
	}
}
