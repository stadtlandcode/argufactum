package de.ifcore.argue.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.ifcore.argue.domain.enumerations.Relevance;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "frv_uk", columnNames = { "registeredUser_id",
		"dedicatedEntity_id" }) })
public final class FactRelevanceVote extends AbstractRelevanceVote<Fact>
{
	private static final long serialVersionUID = -813957621761756808L;

	FactRelevanceVote()
	{
	}

	public FactRelevanceVote(Fact fact, Relevance relevance, Author author)
	{
		super(relevance, fact, author);
	}

	@Override
	protected void addToDedicatedEntity(Fact dedicatedEntity)
	{
		dedicatedEntity.addRelevanceVote(this);
	}
}
