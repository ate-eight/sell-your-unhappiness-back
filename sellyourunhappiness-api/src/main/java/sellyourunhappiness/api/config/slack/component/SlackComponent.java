package sellyourunhappiness.api.config.slack.component;

import org.springframework.stereotype.Component;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SlackComponent {
	private final SlackApi slackApi;

	public void call(SlackMessage slackMessage) {
		slackApi.call(slackMessage);
	}
}

