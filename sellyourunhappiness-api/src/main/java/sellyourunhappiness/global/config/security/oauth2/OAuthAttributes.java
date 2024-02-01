package sellyourunhappiness.global.config.security.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.UserStatus;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.global.config.security.oauth2.abstracts.OAuth2UserInfo;

@Getter
public class OAuthAttributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private OAuth2UserInfo oauth2UserInfo;


	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.oauth2UserInfo = oauth2UserInfo;
	}
	public static OAuthAttributes of(SocialType socialType,
		String userNameAttributeName, Map<String, Object> attributes) {

		if (socialType == SocialType.GOOGLE) {
			return ofGoogle(userNameAttributeName, attributes);
		}
		throw new IllegalArgumentException("Unknown registration ID: " + socialType);
	}


	public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
			.build();
	}


	public User toEntity(SocialType socialType,OAuth2UserInfo oAuth2UserInfo) {
		return User.builder()
			.name(oAuth2UserInfo.getNickname())
			.email(oAuth2UserInfo.getEmail())
			.profileURL(oAuth2UserInfo.getImageUrl())
			.role(Role.GUEST)
			.status(UserStatus.ACTIVE)
			.socialType(socialType)
			.build();
	}
}
