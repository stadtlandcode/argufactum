package de.ifcore.argue.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.ifcore.argue.domain.enumerations.Relevance;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "arv_uk", columnNames = { "registeredUser_id",
		"dedicatedEntity_id" }) })
public final class ArgumentRelevanceVote extends AbstractRelevanceVote<Argument>
{
	private static final long serialVersionUID = -813957621761756808L;

	ArgumentRelevanceVote()
	{
	}

	public ArgumentRelevanceVote(Argument argument, Relevance relevance, Author author)
	{
		super(relevance, argument, author);
	}

	@Override
	protected void addToDedicatedEntity(Argument dedicatedEntity)
	{
		dedicatedEntity.addRelevanceVote(this);
	}
}
