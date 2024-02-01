package sellyourunhappiness.core.user.application;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.core.user.infrastructure.UserRepository;

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
	private static final String BEARER = "Bearer ";

	private final UserRepository userRepository;

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
	 * AccessToken 헤더에 실어서 보내기
	 */
	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader(accessHeader, accessToken);
		log.info("재발급된 Access Token : {}", accessToken);
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
		log.info("Access Token, Refresh Token 헤더 설정 완료");
	}

	/**
	 * AccessToken 헤더 설정
	 */
	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	/**
	 * RefreshToken 헤더 설정
	 */
	public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	/**
	 * RefreshToken DB 저장(업데이트)
	 */
	@Transactional
	public void updateRefreshToken(String email, String refreshToken) {
		userRepository.findByEmail(email)
			.ifPresentOrElse(
				user -> {
					user.updateRefreshToken(refreshToken);
					userRepository.save(user);
				},
				() -> {
					throw new RuntimeException("일치하는 회원이 없습니다.");
				}
			);
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}

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
				throw new RuntimeException("Invalid refresh token.");
			}
		} catch (Exception e) {
			log.error("Error refreshing token: {}", e.getMessage());
			throw new RuntimeException("Error refreshing tokens.");
		}
	}

	/**
	 * AccessToken 유효성 검사 및 새 AccessToken 발급
	 */
	public boolean isTokenExpired(String token) {
		try {
			Date expiration = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getExpiresAt();
			return expiration.before(new Date());
		} catch (Exception e) {
			log.error("Error verifying token expiration: {}", e.getMessage());
			return true;
		}
	}

}
