package de.ifcore.argue.domain.report;

import java.util.Set;

import de.ifcore.argue.domain.entities.ComparisonValueNumberDefinition;
import de.ifcore.argue.domain.enumerations.ComparisonValueInputMethod;
import de.ifcore.argue.domain.enumerations.CriterionDataType;

public interface ComparisonValueDefinitionSetReport extends EntityReport
{
	public String getName();

	public CriterionDataType getDataType();

	public ComparisonValueInputMethod getInputMethod();

	public Integer getSortKey();

	public ComparisonValueNumberDefinition getNumberDefinition();

	public Set<ComparisonValueDefinitionReport> getDefinitionReports();

	public boolean isGlobal();
}
