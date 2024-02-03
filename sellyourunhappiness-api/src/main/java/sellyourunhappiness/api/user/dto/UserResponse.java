package sellyourunhappiness.api.user.dto;

import lombok.Getter;

@Getter
public class UserResponse {
	private String username;
	private String email;

	public UserResponse(String username, String email) {
		this.username = username;
		this.email = email;
	}
}
