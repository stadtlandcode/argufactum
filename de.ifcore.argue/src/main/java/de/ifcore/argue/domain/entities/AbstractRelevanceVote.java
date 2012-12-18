package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import de.ifcore.argue.domain.enumerations.Relevance;

@MappedSuperclass
public abstract class AbstractRelevanceVote<T extends TopicEntityWithRelevance<?>> extends AbstractVote<T> implements
		RelevanceVote
{
	private static final long serialVersionUID = 7674564541929213192L;

	@Column(nullable = false)
	private Byte relevance;

	AbstractRelevanceVote()
	{

	}

	public AbstractRelevanceVote(Relevance relevance, T dedicatedEntity, Author author)
	{
		super(dedicatedEntity, author);
		if (relevance == null)
			throw new IllegalArgumentException();

		this.relevance = relevance.getByteValue();
	}

	@Override
	public Relevance getRelevance()
	{
		return Relevance.forByteValue(relevance);
	}

	@Override
	public Byte getRelevanceByteValue()
	{
		return relevance;
	}

	@Override
	public void edit(Relevance relevance)
	{
		if (relevance == null)
			throw new IllegalArgumentException();
		if (!getRelevance().equals(relevance))
		{
			this.relevance = relevance.getByteValue();
			dedicatedEntity.resetRelevance();
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RelevanceVote [relevance=").append(getRelevance()).append("]");
		return builder.toString();
	}
}
