package sellyourunhappiness.api.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
	private String username;
	private String email;

	public UserInfo() {
	}

	// 생성자
	public UserInfo(String username, String email) {
		this.username = username;
		this.email = email;
	}

}
