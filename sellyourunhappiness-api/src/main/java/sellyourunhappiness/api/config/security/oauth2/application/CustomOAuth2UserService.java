package sellyourunhappiness.api.config.security.oauth2.application;

import static java.util.Collections.*;
import static sellyourunhappiness.api.config.security.oauth2.OAuthAttributes.*;
import static sellyourunhappiness.core.user.domain.enums.SocialType.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.api.config.security.oauth2.OAuthAttributes;
import sellyourunhappiness.api.config.security.oauth2.social.abstarcts.OAuth2UserInfo;
import sellyourunhappiness.api.user.application.UserBroker;
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
		String userNameAttributeName = getUserNameAttributeName(userRequest);
		OAuthAttributes extractAttributes = create(socialType, userNameAttributeName, oAuth2User.getAttributes());

		User user = getUser(socialType, extractAttributes.getOauth2UserInfo());

		return CustomOAuth2User.builder()
			.authorities(singleton(new SimpleGrantedAuthority(user.getRole().getKey())))
			.attributes(oAuth2User.getAttributes())
			.nameAttributeKey(extractAttributes.getNameAttributeKey())
			.email(user.getEmail())
			.role(user.getRole())
			.build();
	}

	private SocialType getSocialType(String registrationId) {
		if ("google".equals(registrationId)) {
			return GOOGLE;
		}
		throw new IllegalArgumentException("SocialType이 일치하지 않습니다.: " + registrationId);
	}

	private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
	}

	private User getUser(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
		return userBroker.getUserByOauth(oAuth2UserInfo.getNickname(), oAuth2UserInfo.getEmail(), oAuth2UserInfo.getImageUrl(), socialType);
	}
}
