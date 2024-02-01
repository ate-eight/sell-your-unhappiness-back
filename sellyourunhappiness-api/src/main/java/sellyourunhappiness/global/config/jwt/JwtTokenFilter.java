package sellyourunhappiness.global.config.jwt;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sellyourunhappiness.core.user.application.JwtService;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException,
		IOException {
		String token = request.getHeader("Authorization");

		if (token != null && token.startsWith("Bearer ")) {
			try {
				token = token.substring(7);
				if (jwtService.isTokenExpired(token)) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "액세스토큰이 만료되었습니다.");
					return;
				}
			} catch (TokenExpiredException ex) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "액세스토큰이 만료되었습니다.");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
