package sellyourunhappiness.api.user.dto;

import java.io.Serializable;

import lombok.Getter;
import sellyourunhappiness.core.user.domain.User;

@Getter
public class SessionUser implements Serializable {
	private String name;
	private String email;
	private String profileURL;

	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.profileURL = user.getProfileURL();
	}
}
