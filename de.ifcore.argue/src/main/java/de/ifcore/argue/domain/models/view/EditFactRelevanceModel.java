package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.FactReport;

public class EditFactRelevanceModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -7387696993596216403L;
	private final RelevanceModel relevance;

	public EditFactRelevanceModel(FactReport fact, MessageSource messageSource)
	{
		super(fact, fact.getTopicId());
		this.relevance = RelevanceModel.instanceOf(fact, "fact." + fact.getType().name(), messageSource);
	}

	public RelevanceModel getRelevance()
	{
		return relevance;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "fact.editRelevance";
	}
}
