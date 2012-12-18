package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.ArgumentReport;

public abstract class AbstractArgumentModel extends AbstractTopicEntityModel
{
	private static final long serialVersionUID = -5298339734062184519L;

	private final Long textId;
	private final String text;

	protected AbstractArgumentModel(ArgumentReport argument)
	{
		super(argument, argument.getTopicId());
		this.textId = argument.getThesisId();
		this.text = argument.getText();

	}

	public Long getTextId()
	{
		return textId;
	}

	public String getText()
	{
		return text;
	}
}
