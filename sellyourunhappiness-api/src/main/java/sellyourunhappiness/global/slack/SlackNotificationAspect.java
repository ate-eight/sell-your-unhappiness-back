package sellyourunhappiness.global.slack;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackNotificationAspect {

	private final SlackApi slackApi;
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;


	@Around("@annotation(sellyourunhappiness.global.slack.SlackNotification)")
	public Object slackNotification(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = proceedingJoinPoint.proceed();

		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Post");
		slackAttachment.setColor("good");
		slackAttachment.setTitle("Data save detected");
		slackAttachment.setFields(Arrays.asList(
			new SlackField().setTitle("Arguments").setValue(Arrays.stream(proceedingJoinPoint.getArgs()).map(Object::toString).collect(
				Collectors.joining(", "))),
			new SlackField().setTitle("Method").setValue(proceedingJoinPoint.getSignature().getName())
		));

		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setAttachments(Arrays.asList(slackAttachment));
		slackMessage.setIcon(":floppy_disk:");
		slackMessage.setText("Post Request");
		slackMessage.setUsername("Sellyourunhappiness");

		threadPoolTaskExecutor.execute(() -> {
			slackApi.call(slackMessage);
		});
		return result;
	}
}
