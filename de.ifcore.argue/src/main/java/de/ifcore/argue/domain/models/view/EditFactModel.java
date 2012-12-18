package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.FactReport;

public class EditFactModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -143634292337630635L;

	private final Long textId;
	private final String text;
	private final LogModel modificationLog;

	public EditFactModel(FactReport fact, Long topicId, MessageSource messageSource)
	{
		super(fact, topicId);
		this.textId = fact.getEvidenceId();
		this.text = fact.getText();
		this.modificationLog = LogModel.instanceOf(fact.getModificationLog(), messageSource);
	}

	@Override
	public String getBroadcastSubject()
	{
		return "fact.edit";
	}

	public Long getTextId()
	{
		return textId;
	}

	public String getText()
	{
		return text;
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EditFactModel [textId=").append(textId).append(", text=").append(text)
				.append(", modificationLog=").append(modificationLog).append("]");
		return builder.toString();
	}
}
