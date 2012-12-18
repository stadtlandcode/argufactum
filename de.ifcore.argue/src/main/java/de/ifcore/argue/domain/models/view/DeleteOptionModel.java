package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.OptionReport;

public class DeleteOptionModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -1091416235152509459L;

	public DeleteOptionModel(OptionReport option)
	{
		super(option, option.getTopicId());
	}

	@Override
	public String getBroadcastSubject()
	{
		return "deleteOption";
	}
}
