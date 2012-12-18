package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.report.RelevanceReport;

public class UserRelevanceModel implements ViewModel
{
	private static final long serialVersionUID = -6625585969836047530L;

	private final Long userId;
	private final Relevance relevance;

	public UserRelevanceModel(Long userId, Relevance relevance)
	{
		this.userId = userId;
		this.relevance = relevance;
	}

	public static Set<UserRelevanceModel> forUsers(Set<Long> userIds, RelevanceReport topicEntity)
	{
		Set<UserRelevanceModel> votes = new HashSet<>();
		for (Long userId : userIds)
		{
			votes.add(new UserRelevanceModel(userId, topicEntity.getRelevanceOfUser(userId)));
		}
		return Collections.unmodifiableSet(votes);
	}

	public Long getUserId()
	{
		return userId;
	}

	public Relevance getRelevance()
	{
		return relevance;
	}
}
