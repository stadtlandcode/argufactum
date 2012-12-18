package de.ifcore.argue.domain.models.view;

import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.ArgumentReport;

public class RestoreArgumentModel extends CreateArgumentModel
{
	private static final long serialVersionUID = -7631194041907407887L;

	private final Set<UserRelevanceModel> userRelevanceVotes;

	public RestoreArgumentModel(ArgumentReport argument, Set<Long> authorUserIds, MessageSource messageSource,
			String url)
	{
		super(argument, messageSource, url);
		this.userRelevanceVotes = UserRelevanceModel.forUsers(authorUserIds, argument);
	}

	public Set<UserRelevanceModel> getUserRelevanceVotes()
	{
		return userRelevanceVotes;
	}
}
