package sellyourunhappiness.global.slack.exception;

import java.util.Collections;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import sellyourunhappiness.global.slack.common.SlackFieldsUtils;
import sellyourunhappiness.global.slack.common.SlackUtils;

@RequiredArgsConstructor
@ControllerAdvice
public class ErrorDetectAdvisor {

	private final SlackUtils slackUtils;

	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest req, Exception e) throws Exception {
		slackUtils.sendSlackMessage(
			"Error Detect",
			e.toString(),
			"danger",
			":ghost:",
			"Sellyourunhappiness",
			Collections.singletonList(
				slackUtils.createSlackAttachment(
					"Error Detect",
					req.getContextPath(),
					e.toString(),
					"danger",
					SlackFieldsUtils.createErrorFields(req, e)
				)
			)
		);
		throw e;
	}
}