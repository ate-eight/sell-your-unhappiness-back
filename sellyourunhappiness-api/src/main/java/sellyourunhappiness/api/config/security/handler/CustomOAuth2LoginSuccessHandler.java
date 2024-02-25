package sellyourunhappiness.api.config.security.handler;

import java.io.IOException;

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
				generateAndSaveTokens(user);
				response.sendRedirect("/v1/login/success");
			} else if (jwtService.isTokenExpired(user.getAccessToken())){
				handleTokenRenewal(response,user);
			} else {
				log.info("User " + email + " 님이 유효한 액세스 토큰으로 성공적으로 로그인했습니다.");
			}
		} catch (Exception e) {
			throw new RuntimeException("인증 성공 처리 중 오류 발생", e);
		}
	}

	private void handleTokenRenewal(HttpServletResponse response, User user) throws IOException {
		if (!jwtService.isTokenValid(user.getRefreshToken()) || jwtService.isTokenExpired(user.getRefreshToken())) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다. 다시 로그인해주세요.");
			return;
		}
		tokenValidity(response, user);
	}

	private void tokenValidity(HttpServletResponse response, User user) throws IOException {
		if (jwtService.getRemainingDays(user.getRefreshToken()) <= 3) {
			generateAndSaveTokens(user);
		} else {
			String newAccessToken = jwtService.createAccessToken(user.getEmail());
			jwtService.updateAccessToken(user.getEmail(), newAccessToken);
			response.sendRedirect("/v1/login/success");
		}
	}

	private void generateAndSaveTokens(User user){
		String newAccessToken = jwtService.createAccessToken(user.getEmail());
		String newRefreshToken = jwtService.createRefreshToken();
		jwtService.updateJwtToken(user.getEmail(), newAccessToken, newRefreshToken);
	}
}