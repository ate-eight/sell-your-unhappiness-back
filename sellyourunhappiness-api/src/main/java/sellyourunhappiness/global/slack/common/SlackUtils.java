package sellyourunhappiness.global.slack.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

@Component
public class SlackUtils {

	private final SlackApi slackApi;

	@Autowired
	public SlackUtils(SlackApi slackApi) {
		this.slackApi = slackApi;
	}

	public void sendSlackMessage(String title, String text, String color, String icon, String username, List<SlackAttachment> attachments) {
		SlackMessage slackMessage = new SlackMessage()
			.setIcon(icon)
			.setText(text)
			.setUsername(username)
			.setAttachments(attachments);

		slackApi.call(slackMessage);
	}

	public SlackAttachment createSlackAttachment(String title, String titleLink, String text, String color, List<SlackField> fields) {
		return new SlackAttachment()
			.setFallback(title)
			.setColor(color)
			.setTitle(title)
			.setTitleLink(titleLink)
			.setText(text)
			.setFields(fields);
	}
}
