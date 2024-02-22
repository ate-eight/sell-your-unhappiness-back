package sellyourunhappiness.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import sellyourunhappiness.api.config.jwt.application.JwtService;
import sellyourunhappiness.api.config.slack.component.SlackComponent;

@TestConfiguration
public class MvcTestConfig {
	@MockBean
	private SlackComponent slackComponent;
	@MockBean
	private JwtService jwtService;
}
