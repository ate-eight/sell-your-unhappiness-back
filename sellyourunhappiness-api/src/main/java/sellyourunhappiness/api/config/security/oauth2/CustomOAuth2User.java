package sellyourunhappiness.api.config.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.core.user.domain.enums.Role;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
	private String email;
	private Role role;

	@Builder
	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String email, Role role) {
		super(authorities, attributes, nameAttributeKey);
		this.email = email;
		this.role = role;
	}
}