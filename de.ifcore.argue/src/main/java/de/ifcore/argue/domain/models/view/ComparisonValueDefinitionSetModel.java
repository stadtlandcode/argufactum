package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import de.ifcore.argue.domain.entities.ComparisonValueNumberDefinition;
import de.ifcore.argue.domain.enumerations.ComparisonValueInputMethod;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionReport;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;

public class ComparisonValueDefinitionSetModel implements ViewModel
{
	private static final long serialVersionUID = 7975010024129099532L;

	private final Long id;
	private final String name;
	private final Integer sortKey;
	private final boolean global;
	private final CriterionDataType dataType;
	private final ComparisonValueInputMethod inputMethod;
	private final ComparisonValueNumberDefinition numberDefinition;
	private final Set<ComparisonValueDefinitionModel> definitions;

	public ComparisonValueDefinitionSetModel(ComparisonValueDefinitionSetReport definitionSetReport)
	{
		this.id = definitionSetReport.getId();
		this.name = definitionSetReport.getName();
		this.sortKey = definitionSetReport.getSortKey();
		this.global = definitionSetReport.isGlobal();
		this.dataType = definitionSetReport.getDataType();
		this.inputMethod = definitionSetReport.getInputMethod();
		this.numberDefinition = definitionSetReport.getNumberDefinition();
		this.definitions = createDefinitionSet(definitionSetReport.getDefinitionReports());
	}

	public String getName()
	{
		return name;
	}

	public CriterionDataType getDataType()
	{
		return dataType;
	}

	public Set<ComparisonValueDefinitionModel> getDefinitions()
	{
		return definitions;
	}

	public Long getId()
	{
		return id;
	}

	public Integer getSortKey()
	{
		return sortKey;
	}

	public ComparisonValueInputMethod getInputMethod()
	{
		return inputMethod;
	}

	public boolean isGlobal()
	{
		return global;
	}

	public ComparisonValueNumberDefinition getNumberDefinition()
	{
		return numberDefinition;
	}

	private Set<ComparisonValueDefinitionModel> createDefinitionSet(
			Set<ComparisonValueDefinitionReport> definitionReports)
	{
		Set<ComparisonValueDefinitionModel> definitions = new LinkedHashSet<>();
		for (ComparisonValueDefinitionReport definitionReport : definitionReports)
			definitions.add(new ComparisonValueDefinitionModel(definitionReport));
		return Collections.unmodifiableSet(definitions);
	}

	public static Set<ComparisonValueDefinitionSetModel> convert(Set<ComparisonValueDefinitionSetReport> definitions)
	{
		Set<ComparisonValueDefinitionSetModel> definitionSets = new LinkedHashSet<>();
		for (ComparisonValueDefinitionSetReport definitionSetReport : definitions)
			definitionSets.add(new ComparisonValueDefinitionSetModel(definitionSetReport));
		return Collections.unmodifiableSet(definitionSets);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CreateComparisonValueDefinitionSetModel [name=").append(name).append(", dataType=")
				.append(dataType).append(", definitions=").append(definitions).append("]");
		return builder.toString();
	}
}
