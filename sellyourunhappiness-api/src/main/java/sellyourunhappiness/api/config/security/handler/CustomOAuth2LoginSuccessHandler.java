package sellyourunhappiness.api.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
			loginSuccess(response, oAuth2User);
		} catch (Exception e) {
			throw e;
		}
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {
		String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
		String refreshToken = jwtService.createRefreshToken();
		response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
	}
}
