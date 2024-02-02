package sellyourunhappiness.api.config.exception;

import java.util.Arrays;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.slack.aspect.dto.SlackAttachmentModel;
import sellyourunhappiness.api.config.slack.aspect.dto.SlackMessageModel;
import sellyourunhappiness.api.config.slack.component.SlackComponent;
import sellyourunhappiness.api.config.slack.utils.SlackFieldsUtils;

@RequiredArgsConstructor
@ControllerAdvice
public class ErrorDetectAdvisor {

	private final SlackComponent slackComponent;

	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest req, Exception e) throws Exception {
		SlackAttachmentModel attachmentModel = SlackAttachmentModel.builder()
				.title("Error Detect")
				.titleLink(req.getContextPath())
				.attachmentText(e.toString())
				.color("danger")
				.fields(SlackFieldsUtils.createErrorFields(req))
				.build();

		SlackMessageModel slackMessageModel = SlackMessageModel.builder()
				.messageText(e.toString())
				.icon(":ghost:")
				.username("Sellyourunhappiness")
				.attachments(Arrays.asList(attachmentModel.createSlackAttachment()))
				.build();

		slackComponent.call(slackMessageModel.createSlackMessage());

		throw e;
	}
}