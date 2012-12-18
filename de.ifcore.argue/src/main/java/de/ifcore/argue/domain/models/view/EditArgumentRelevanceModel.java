package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.ArgumentReport;

public class EditArgumentRelevanceModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -7387696993596216403L;
	private final RelevanceModel relevance;

	public EditArgumentRelevanceModel(ArgumentReport argument, MessageSource messageSource)
	{
		super(argument, argument.getTopicId());
		this.relevance = RelevanceModel.instanceOf(argument, "argument", messageSource);
	}

	public RelevanceModel getRelevance()
	{
		return relevance;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "argument.editRelevance";
	}
}
