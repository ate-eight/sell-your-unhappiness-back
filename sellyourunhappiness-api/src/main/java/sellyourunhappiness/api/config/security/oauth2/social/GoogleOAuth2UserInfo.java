package sellyourunhappiness.api.config.security.oauth2.social;

import java.util.Map;

import sellyourunhappiness.api.config.security.oauth2.social.abstracts.OAuth2UserInfo;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String)attributes.get("sub");
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getNickname() {
		return (String)attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String)attributes.get("picture");
	}
}
