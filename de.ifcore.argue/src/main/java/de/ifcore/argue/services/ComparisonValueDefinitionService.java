package de.ifcore.argue.services;

import java.util.SortedSet;

import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;
import de.ifcore.argue.domain.report.CriterionReport;

public interface ComparisonValueDefinitionService extends Service
{
	public ComparisonValueDefinitionSet getDefinitionSet(Long id);

	public SortedSet<ComparisonValueDefinitionSetReport> getAdditionalSets(CriterionReport criterion);

	public SortedSet<ComparisonValueDefinitionSetReport> getGlobalSets();
}
