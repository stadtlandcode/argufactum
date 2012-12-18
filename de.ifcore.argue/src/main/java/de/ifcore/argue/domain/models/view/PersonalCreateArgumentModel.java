package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.report.ArgumentReport;

public class PersonalCreateArgumentModel extends CreateArgumentModel
{
	private static final long serialVersionUID = 4209292201558873329L;

	private final Relevance personalRelevance;

	public PersonalCreateArgumentModel(ArgumentReport argument, Long userId, MessageSource messageSource, String url)
	{
		super(argument, messageSource, url);
		this.personalRelevance = userId == null ? null : argument.getRelevanceOfUser(userId);
	}

	public Relevance getPersonalRelevance()
	{
		return personalRelevance;
	}
}
