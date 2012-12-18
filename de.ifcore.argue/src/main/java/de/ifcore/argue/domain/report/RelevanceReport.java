package de.ifcore.argue.domain.report;

import java.math.BigDecimal;

import de.ifcore.argue.domain.enumerations.Relevance;

public interface RelevanceReport
{
	public Relevance getRelevance();

	public BigDecimal getNumericRelevance();

	public Relevance getRelevanceOfUser(Long userId);
}
