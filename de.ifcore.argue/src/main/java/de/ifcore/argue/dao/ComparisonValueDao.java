package de.ifcore.argue.dao;

import de.ifcore.argue.domain.entities.ComparisonValue;

public interface ComparisonValueDao
{
	public ComparisonValue get(Long criterionId, Long optionId);

	public void save(ComparisonValue value);

	public void update(ComparisonValue value);
}
