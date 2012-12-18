package de.ifcore.argue.domain.models.view;

import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.FactReport;

public class RestoreFactModel extends CreateFactModel
{
	private static final long serialVersionUID = -4079328594091259050L;

	private final Set<UserRelevanceModel> userRelevanceVotes;

	public RestoreFactModel(FactReport fact, Set<Long> authorUserIds, MessageSource messageSource)
	{
		super(fact, messageSource);
		this.userRelevanceVotes = UserRelevanceModel.forUsers(authorUserIds, fact);
	}

	public Set<UserRelevanceModel> getUserRelevanceVotes()
	{
		return userRelevanceVotes;
	}
}
