package sellyourunhappiness.api.config.slack.aspect.dto;

import java.util.List;

import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;

import lombok.Builder;

public class SlackMessageModel {
	private String messageText;
	private String icon;
	private String username;
	private List<SlackAttachment> attachments;

	@Builder
	public SlackMessageModel(String messageText, String icon, String username, List<SlackAttachment> attachments) {
		this.messageText = messageText;
		this.icon = icon;
		this.username = username;
		this.attachments = attachments;
	}

	public SlackMessage createSlackMessage() {
		return new SlackMessage()
			.setIcon(icon)
			.setText(messageText)
			.setUsername(username)
			.setAttachments(attachments);
	}
}
