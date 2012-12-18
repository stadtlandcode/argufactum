package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.report.FactReport;

public class PersonalCreateFactModel extends CreateFactModel
{
	private static final long serialVersionUID = 9155664967083451560L;

	private final Relevance personalRelevance;

	public PersonalCreateFactModel(FactReport fact, Long userId, MessageSource messageSource)
	{
		super(fact, messageSource);
		this.personalRelevance = userId == null ? null : fact.getRelevanceOfUser(userId);
	}

	public Relevance getPersonalRelevance()
	{
		return personalRelevance;
	}
}
