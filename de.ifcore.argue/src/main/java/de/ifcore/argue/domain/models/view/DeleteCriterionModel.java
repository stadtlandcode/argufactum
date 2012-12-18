package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.CriterionReport;

public class DeleteCriterionModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 1890682162927529112L;

	public DeleteCriterionModel(CriterionReport criterion)
	{
		super(criterion, criterion.getTopicId());
	}

	@Override
	public String getBroadcastSubject()
	{
		return "deleteCriterion";
	}
}
