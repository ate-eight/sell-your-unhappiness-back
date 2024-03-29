package sellyourunhappiness.util.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.gpedro.integrations.slack.SlackApi;

@Configuration
public class SlackLogAppenderConfig {

	@Value("${sellyourunhappiness.slack.token}")
	private String token;

	@Bean
	public SlackApi slackApi() {
		return new SlackApi("https://hooks.slack.com/services/" + token);
	}
}
