package sellyourunhappiness.global.slack.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.global.slack.common.SlackFieldsUtils;
import sellyourunhappiness.global.slack.common.SlackUtils;

@RequiredArgsConstructor
@Aspect
@Component
public class SlackNotificationAspect {

	private final SlackUtils slackUtils;

	@Around("@annotation(sellyourunhappiness.global.slack.annotation.SlackNotification)")
	public Object slackNotification(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = proceedingJoinPoint.proceed();

		slackUtils.sendSlackMessage(
			"Data save detected",
			"Post Request",
			"good",
			":floppy_disk:",
			"Sellyourunhappiness",
			Arrays.asList(
				slackUtils.createSlackAttachment(
					"Data save detected",
					null,
					"A data save operation was detected.",
					"good",
					SlackFieldsUtils.createNotificationFields(proceedingJoinPoint)
				)
			)
		);
		return result;
	}
}