package sellyourunhappiness.api.config.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponseCommon;

@Component
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		ApiResponseCommon apiResponseCommon = ApiResponseCommon.internalServerError();
		ApiResponse apiResponse = ApiResponse.create(apiResponseCommon);

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
	}
}