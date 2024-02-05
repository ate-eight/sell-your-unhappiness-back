package sellyourunhappiness.api.config.slack.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.config.slack.aspect.dto.SlackAttachmentModel;
import sellyourunhappiness.api.config.slack.aspect.dto.SlackMessageModel;
import sellyourunhappiness.api.config.slack.component.SlackComponent;
import sellyourunhappiness.api.config.slack.utils.SlackFieldsUtils;

@RequiredArgsConstructor
@Aspect
@Component
public class SlackNotificationAspect {

	private final SlackComponent slackComponent;

	@Around("@annotation(sellyourunhappiness.api.config.slack.annotation.SlackNotification)")
	public Object slackNotification(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = proceedingJoinPoint.proceed();

		SlackAttachmentModel attachmentModel = SlackAttachmentModel.builder()
			.title("Data save detected")
			.titleLink(null)
			.attachmentText("A data save operation was detected.")
			.color("good")
			.fields(SlackFieldsUtils.createNotificationFields(proceedingJoinPoint))
			.build();

		SlackMessageModel slackMessageModel = SlackMessageModel.builder()
			.messageText("Post Request")
			.icon(":floppy_disk:")
			.username("Sellyourunhappiness")
			.attachments(Arrays.asList(attachmentModel.createSlackAttachment()))
			.build();

		slackComponent.call(slackMessageModel.createSlackMessage());

		return result;
	}
}