package sellyourunhappiness.api.config.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponseCommon;

@Component
public class CustomOAuth2AccessDeniedHandler implements AccessDeniedHandler {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		ApiResponseCommon apiResponseCommon = ApiResponseCommon.internalServerError();
		ApiResponse apiResponse = ApiResponse.create(apiResponseCommon);

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
	}
}
