package sellyourunhappiness.api.config.slack.aspect.dto;

import java.util.List;

import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;

import lombok.Builder;

public class SlackAttachmentModel {
	private String title;
	private String titleLink;
	private String attachmentText;
	private String color;
	private List<SlackField> fields;

	@Builder
	public SlackAttachmentModel(String title, String titleLink, String attachmentText, String color, List<SlackField> fields) {
		this.title = title;
		this.titleLink = titleLink;
		this.attachmentText = attachmentText;
		this.color = color;
		this.fields = fields;
	}

	public SlackAttachment createSlackAttachment() {
		return new SlackAttachment()
			.setFallback(title)
			.setColor(color)
			.setTitle(title)
			.setTitleLink(titleLink)
			.setText(attachmentText)
			.setFields(fields);
	}
}
