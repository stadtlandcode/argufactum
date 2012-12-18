package de.ifcore.argue.domain.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.utils.EntityUtils;

@MappedSuperclass
public abstract class AbstractTopicEntityWithRelevance<T extends RelevanceVote> extends AbstractDeletableTopicEntity
		implements TopicEntityWithRelevance<T>
{
	private static final long serialVersionUID = -4253415644493275676L;

	AbstractTopicEntityWithRelevance()
	{
	}

	AbstractTopicEntityWithRelevance(Author author)
	{
		super(author);
	}

	AbstractTopicEntityWithRelevance(Author author, int timeDiff)
	{
		super(author, timeDiff);
	}

	@OneToMany(mappedBy = "dedicatedEntity", cascade = CascadeType.PERSIST)
	private Set<T> relevanceVotes;

	@Transient
	private transient BigDecimal relevance;

	@Override
	public Relevance getRelevance()
	{
		if (getRelevanceVotes().isEmpty())
			return null;
		else
			return Relevance.forByteValue(Byte.valueOf(getNumericRelevance().setScale(0, RoundingMode.HALF_UP)
					.byteValueExact()));
	}

	@Override
	public BigDecimal getNumericRelevance()
	{
		if (this.relevance == null)
		{
			int relevance = 0;
			for (T relevanceVote : getRelevanceVotes())
				relevance += relevanceVote.getRelevanceByteValue().intValue();

			if (relevance > 0)
				this.relevance = new BigDecimal(relevance).divide(new BigDecimal(getRelevanceVotes().size()), 2,
						RoundingMode.HALF_UP);
			else
				this.relevance = BigDecimal.ZERO;
		}
		return this.relevance;
	}

	@Override
	public Relevance getRelevanceOfUser(Long userId)
	{
		T relevanceVote = getRelevanceVoteOfUser(userId);
		return relevanceVote == null ? null : relevanceVote.getRelevance();
	}

	@Override
	public T getRelevanceVoteOfUser(Long userId)
	{
		if (userId == null)
			throw new IllegalArgumentException();
		for (T relevanceVote : getRelevanceVotes())
		{
			if (userId.equals(relevanceVote.getUserId()))
				return relevanceVote;
		}
		return null;
	}

	@Override
	public Set<T> getRelevanceVotes()
	{
		return relevanceVotes == null ? Collections.<T> emptySet() : Collections.unmodifiableSet(relevanceVotes);
	}

	@Override
	public void addRelevanceVote(T vote)
	{
		if (vote == null || vote.getDedicatedEntity() != this)
			throw new IllegalArgumentException();

		if (relevanceVotes == null)
			relevanceVotes = new HashSet<>();
		else if (EntityUtils.isDuplicateVote(relevanceVotes, vote))
			throw new DuplicateVoteException();

		relevanceVotes.add(vote);
		relevance = null;
	}

	@Override
	public void deleteRelevanceVote(T vote)
	{
		if (vote == null || vote.getDedicatedEntity() != this)
			throw new IllegalArgumentException();

		if (relevanceVotes.remove(vote))
			resetRelevance();
	}

	@Override
	public void resetRelevance()
	{
		relevance = null;
	}
}
