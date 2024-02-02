package sellyourunhappiness.api.config.slack.component;

import lombok.RequiredArgsConstructor;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SlackComponent {

    private final SlackApi slackApi;

    public void call(SlackMessage slackMessage) {
        slackApi.call(slackMessage);
    }
}

