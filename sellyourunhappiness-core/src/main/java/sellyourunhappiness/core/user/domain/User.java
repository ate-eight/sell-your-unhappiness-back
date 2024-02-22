package sellyourunhappiness.core.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sellyourunhappiness.core.user.domain.enums.Role;
import sellyourunhappiness.core.user.domain.enums.SocialType;
import sellyourunhappiness.core.user.domain.enums.UserStatus;

@Table(name = "Users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(name = "email", unique = true)
	private String email;
	private String profileURL;
	@Enumerated(EnumType.STRING)
	private Role role;
	@Enumerated(EnumType.STRING)
	private SocialType socialType;
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	private String accessToken;
	private String refreshToken;

	@Builder
	public User(String name, String email, String profileURL, Role role, UserStatus status, SocialType socialType) {
		this.name = name;
		this.email = email;
		this.profileURL = profileURL;
		this.role = role;
		this.status = status;
		this.socialType = socialType;
	}

	public static User create(String name, String email, String profileUrl, SocialType socialType) {
		return User.builder()
			.name(name)
			.email(email)
			.profileURL(profileUrl)
			.role(Role.USER)
			.status(UserStatus.ACTIVE)
			.socialType(socialType)
			.build();
	}

	public void updateJwtToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public void updateAccessToken(String accessToken){
		this.accessToken = accessToken;
	}
}
