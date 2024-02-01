package sellyourunhappiness.global.slack.common;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;

import net.gpedro.integrations.slack.SlackField;

import jakarta.servlet.http.HttpServletRequest;

public class SlackFieldsUtils {
	public static List<SlackField> createErrorFields(HttpServletRequest req, Exception e) {
		return Arrays.asList(
			new SlackField().setTitle("Request URL").setValue(req.getRequestURL().toString()),
			new SlackField().setTitle("Request Method").setValue(req.getMethod()),
			new SlackField().setTitle("Request Time").setValue(new Date().toString()),
			new SlackField().setTitle("Request IP").setValue(req.getRemoteAddr()),
			new SlackField().setTitle("Request User-Agent").setValue(req.getHeader("User-Agent"))
		);
	}

	public static List<SlackField> createNotificationFields(ProceedingJoinPoint proceedingJoinPoint) {
		return Arrays.asList(
			new SlackField().setTitle("Arguments").setValue(Arrays.stream(proceedingJoinPoint.getArgs())
				.map(Object::toString).collect(Collectors.joining(", "))),
			new SlackField().setTitle("Method").setValue(proceedingJoinPoint.getSignature().getName())
		);
	}
}
