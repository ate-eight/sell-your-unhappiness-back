package sellyourunhappiness.api.config.slack.component;

import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SlackComponent {
	private final SlackApi slackApi;

	public void call(SlackMessage slackMessage) {
		slackApi.call(slackMessage);
	}
}

