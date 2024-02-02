package sellyourunhappiness.api.config.security.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.api.config.security.oauth2.social.GoogleOAuth2UserInfo;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.api.config.security.oauth2.social.abstracts.OAuth2UserInfo;

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

	public static OAuthAttributes create(SocialType socialType,
		String userNameAttributeName, Map<String, Object> attributes) {

		if (socialType == SocialType.GOOGLE) {
			return ofGoogle(userNameAttributeName, attributes);
		}
		throw new IllegalArgumentException("SocialType을 찾을 수 없습니다.: " + socialType);
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
			.build();
	}
}
