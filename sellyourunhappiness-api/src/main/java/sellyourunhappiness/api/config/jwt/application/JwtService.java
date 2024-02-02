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

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
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

	/**
	 * RefreshToken 유효성 검사 및 새 AccessToken 발급
	 */
	public Map<String, String> refreshAccessToken(String refreshToken) {
		try {
			if (isTokenValid(refreshToken)) {
				String email = JWT.decode(refreshToken).getClaim(EMAIL_CLAIM).asString();
				String newAccessToken = createAccessToken(email);

				if (isTokenExpired(refreshToken)) {
					String newRefreshToken = createRefreshToken();
					updateRefreshToken(email, newRefreshToken);
					return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
				}

				return Map.of("accessToken", newAccessToken);
			} else {
				throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
			}
		} catch (Exception e) {
			throw new RuntimeException("토큰을 새로고침하는 동안 오류가 발생했습니다.");
		}
	}

	/**
	 * AccessToken 생성 메소드
	 */
	public String createAccessToken(String email) {
		Date now = new Date();
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
			.withClaim(EMAIL_CLAIM, email)
			.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * RefreshToken 생성 메소드
	 */
	public String createRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * RefreshToken DB 저장(업데이트)
	 */
	@Transactional
	public void updateRefreshToken(String email, String refreshToken) {
		User user = userService.findByEmail(email);
		user.updateRefreshToken(refreshToken);
		userService.save(user.getName(), user.getEmail(), user.getProfileURL(), user.getSocialType());
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
	}

	/**
	 * AccessToken 헤더 설정
	 */
	private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	/**
	 * RefreshToken 헤더 설정
	 */
	private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	/**
	 * 토큰 유효성 검사
	 */
	private boolean isTokenValid(String token) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}

	/**
	 * AccessToken 유효성 검사
	 */
	public boolean isTokenExpired(String token) {
		try {
			Date expiration = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getExpiresAt();
			return expiration.before(new Date());
		} catch (JWTVerificationException e) {
			return true;
		}
	}
}
