package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;

public interface ComparisonValueDefinitionDao
{
	public ComparisonValueDefinitionSet getSet(Long id);

	public void saveSet(ComparisonValueDefinitionSet definitionSet);

	public List<ComparisonValueDefinitionSet> getGlobalSets();
}
