package sellyourunhappiness.global.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@ControllerAdvice
@RequiredArgsConstructor
public class ErrorDetectAdvisor {


	private final SlackApi slackApi;


	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest req, Exception e) throws Exception {

		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("danger");
		slackAttachment.setTitle("Error Detect");
		slackAttachment.setTitleLink(req.getContextPath());
		slackAttachment.setText(e.toString());

		slackAttachment.setFields(Arrays.asList(
			new SlackField().setTitle("Request URL").setValue(req.getRequestURL().toString()),
			new SlackField().setTitle("Request Method").setValue(req.getMethod()),
			new SlackField().setTitle("Request Time").setValue(new Date().toString()),
			new SlackField().setTitle("Request IP").setValue(req.getRemoteAddr()),
			new SlackField().setTitle("Request User-Agent").setValue(req.getHeader("User-Agent"))
		));

		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setAttachments(Collections.singletonList(slackAttachment));
		slackMessage.setIcon(":ghost:");
		slackMessage.setText("Error Detect");
		slackMessage.setUsername("Sellyourunhappiness");

		slackApi.call(slackMessage);

		throw e;
	}

}