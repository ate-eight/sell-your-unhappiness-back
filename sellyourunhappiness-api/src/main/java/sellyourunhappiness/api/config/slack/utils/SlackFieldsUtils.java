package sellyourunhappiness.api.config.slack.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import net.gpedro.integrations.slack.SlackField;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlackFieldsUtils {
	public static List<SlackField> createErrorFields(HttpServletRequest req) {
		return Arrays.asList(
			createSlackField("Request URL", req.getRequestURL().toString()),
			createSlackField("Request Method", req.getMethod()),
			createSlackField("Request Time", new Date().toString()),
			createSlackField("Request IP", req.getRemoteAddr()),
			createSlackField("Request User-Agent", req.getHeader("User-Agent"))
		);
	}

	public static List<SlackField> createNotificationFields(ProceedingJoinPoint proceedingJoinPoint) {
		String arguments = Arrays.stream(proceedingJoinPoint.getArgs())
			.map(Object::toString)
			.collect(Collectors.joining(", "));

		String jointPointName = proceedingJoinPoint.getSignature().getName();

		return Arrays.asList(
			createSlackField("Arguments", arguments),
			createSlackField("Method", jointPointName)
		);
	}

	private static SlackField createSlackField(String title, String value) {
		return new SlackField().setTitle(title).setValue(value);
	}
}
