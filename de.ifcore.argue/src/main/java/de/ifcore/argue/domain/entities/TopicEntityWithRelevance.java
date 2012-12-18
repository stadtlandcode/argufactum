package de.ifcore.argue.domain.entities;

import java.math.BigDecimal;
import java.util.Set;

import de.ifcore.argue.domain.enumerations.Relevance;

public interface TopicEntityWithRelevance<T extends RelevanceVote> extends DeletableTopicEntity
{
	public Relevance getRelevance();

	public BigDecimal getNumericRelevance();

	public Relevance getRelevanceOfUser(Long userId);

	public T getRelevanceVoteOfUser(Long userId);

	void addRelevanceVote(T vote);

	public void deleteRelevanceVote(T vote);

	public Set<T> getRelevanceVotes();

	void resetRelevance();
}
