package de.ifcore.argue.domain.entities;

import de.ifcore.argue.domain.enumerations.Relevance;

public interface RelevanceVote extends Vote
{
	public Relevance getRelevance();

	Byte getRelevanceByteValue();

	public void edit(Relevance relevance);
}
