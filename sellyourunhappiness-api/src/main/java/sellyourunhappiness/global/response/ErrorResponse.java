package sellyourunhappiness.global.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

	private final HttpStatus status;
	private final String message;
}
