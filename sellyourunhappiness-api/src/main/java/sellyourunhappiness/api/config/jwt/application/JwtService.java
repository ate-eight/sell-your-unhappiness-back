package sellyourunhappiness.api.config.jwt.application;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JwtService {
	@Value("${jwt.token.key}")
	private String secretKey;
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;
	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;
	@Value("${jwt.access.header}")
	private String accessHeader;
	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String EMAIL_CLAIM = "email";
	private final UserService userService;

	public String createAccessToken(String email) {
		Date now = new Date();
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
			.withClaim(EMAIL_CLAIM, email)
			.sign(Algorithm.HMAC512(secretKey));
	}

	public String createRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
	}

	private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	@Transactional
	public void updateAccessToken(String email, String accesstoken){
		User user = userService.findByEmail(email);
		user.updateAccessToken(accesstoken);
	}
	@Transactional
	public void updateRefreshToken(String email, String refreshtoken){
		User user = userService.findByEmail(email);
		user.updateAccessToken(refreshtoken);
	}
	@Transactional
	public void updateJwtToken(String email, String accesstoken, String refreshToken) {
		User user = userService.findByEmail(email);
		user.updateJwtToken(accesstoken, refreshToken);
	}

	public boolean isTokenValid(String token) {
		if (token == null) {
			return false;
		}
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}

	public Map<String, String> refreshAccessToken(String refreshToken) {
		if (!isTokenValid(refreshToken)) {
			throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
		}

		String email = JWT.decode(refreshToken).getClaim(EMAIL_CLAIM).asString();
		String newAccessToken = createAccessToken(email);

		if (isTokenExpired(refreshToken)) {
			String newRefreshToken = createRefreshToken();
			updateRefreshToken(email, newRefreshToken);
			return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
		}

		return Map.of("accessToken", newAccessToken);
	}

	public boolean isTokenExpired(String token) {
		if (token == null) {
			return true;
		}
		try {
			Date expiration = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getExpiresAt();
			return expiration.before(new Date());
		} catch (JWTVerificationException e) {
			return true;
		}
	}

	public long getRemainingDays(String token) {
		try {
			DecodedJWT decodedJWT = JWT.decode(token);
			Date expirationDate = decodedJWT.getExpiresAt();
			if (expirationDate == null) {
				return 0;
			}
			long difference = expirationDate.getTime() - System.currentTimeMillis();
			return Math.max(0, difference / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			return 0;
		}
	}
}
