package sellyourunhappiness.api.config.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	private final UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
			String email = oAuth2User.getEmail();
			User user = userService.findByEmail(email);

			if (user.getAccessToken() == null) {
				generateAndSaveTokens(response, user);
			} else if (jwtService.isTokenExpired(user.getAccessToken())){
				handleTokenRenewal(response,user);
			} else {
				log.info("User " + email + " 님이 유효한 액세스 토큰으로 성공적으로 로그인했습니다.");
			}
		} catch (Exception e) {
			throw new RuntimeException("인증 성공 처리 중 오류 발생", e);
		}
	}

	private void handleTokenRenewal(HttpServletResponse response, User user) {
		if (!jwtService.isTokenValid(user.getRefreshToken())) {
			throw new AccessDeniedException("토큰이 유효하지 않습니다. 다시 로그인해주세요.");
		}

		if (jwtService.isTokenExpired(user.getRefreshToken())) {
			throw new AccessDeniedException("토큰이 만료되었습니다. 다시 로그인해주세요.");
		}
		tokenValidity(response, user);
	}

	private void tokenValidity(HttpServletResponse response, User user) {
		if (jwtService.getRemainingDays(user.getRefreshToken()) <= 3) {
			generateAndSaveTokens(response, user);
		} else {
			String newAccessToken = jwtService.createAccessToken(user.getEmail());
			jwtService.updateAccessToken(user.getEmail(), newAccessToken);
			jwtService.sendAccessAndRefreshToken(response, newAccessToken, user.getRefreshToken());
		}
	}

	private void generateAndSaveTokens(HttpServletResponse response, User user) {
		String newAccessToken = jwtService.createAccessToken(user.getEmail());
		String newRefreshToken = jwtService.createRefreshToken();
		jwtService.updateJwtToken(user.getEmail(), newAccessToken, newRefreshToken);
		jwtService.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);
	}
}