package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.ComparisonValueReport;

public class ComparisonValueModel extends AbstractTopicBroadcast implements ViewModel
{
	private static final long serialVersionUID = -8083753268572110724L;

	private final Long criterionId;
	private final Long optionId;
	private final String string;

	public ComparisonValueModel(ComparisonValueReport value, Long topicId)
	{
		super(topicId);
		this.criterionId = value.getCriterionId();
		this.optionId = value.getOptionId();
		this.string = value.getString();
	}

	@Override
	public String getBroadcastSubject()
	{
		return "value";
	}

	public Long getCriterionId()
	{
		return criterionId;
	}

	public Long getOptionId()
	{
		return optionId;
	}

	public String getString()
	{
		return string;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValueModel [criterionId=").append(criterionId).append(", optionId=").append(optionId)
				.append(", value=").append(string).append("]");
		return builder.toString();
	}
}
