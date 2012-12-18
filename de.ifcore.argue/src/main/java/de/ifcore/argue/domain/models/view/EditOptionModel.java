package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.OptionReport;

public class EditOptionModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -4573589179460223488L;

	private final String label;

	public EditOptionModel(OptionReport option)
	{
		super(option, option.getTopicId());
		this.label = option.getLabel();
	}

	@Override
	public String getBroadcastSubject()
	{
		return "editOption";
	}

	public String getLabel()
	{
		return label;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EditOptionModel [label=").append(label).append(", getId()=").append(getId()).append("]");
		return builder.toString();
	}
}
