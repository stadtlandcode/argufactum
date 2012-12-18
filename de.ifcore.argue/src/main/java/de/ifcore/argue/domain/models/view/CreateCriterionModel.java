package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;
import de.ifcore.argue.domain.report.ComparisonValueReport;
import de.ifcore.argue.domain.report.CriterionReport;

// TODO readonly model nur mit aktuellem definitionSet
public class CreateCriterionModel extends EditCriterionModel implements ViewModel
{
	private static final long serialVersionUID = -3040694097860015992L;

	private final Byte importance;
	private final Set<ComparisonValueModel> values;
	private final Long processId;

	public CreateCriterionModel(CriterionReport criterion, RegisteredUser user, Long processId,
			SortedSet<ComparisonValueDefinitionSetReport> additionalDefinitionSets)
	{
		super(criterion, additionalDefinitionSets);
		this.importance = criterion.getImportance(user);
		this.values = createValueSet(criterion);
		this.processId = processId;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "createCriterion";
	}

	public Long getRequestId()
	{
		return processId;
	}

	public Byte getImportance()
	{
		return importance;
	}

	public Set<ComparisonValueModel> getValues()
	{
		return values;
	}

	private Set<ComparisonValueModel> createValueSet(CriterionReport criterion)
	{
		Set<ComparisonValueModel> valueModels = new HashSet<>();
		for (ComparisonValueReport value : criterion.getValues())
			valueModels.add(new ComparisonValueModel(value, criterion.getTopicId()));
		return Collections.unmodifiableSet(valueModels);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CreateCriterionModel [id=").append(getId()).append(", label=").append(getLabel())
				.append(", definitionSetId=").append(getDefinitionSetId()).append(", importance=").append(importance)
				.append(", values=").append(values).append("]");
		return builder.toString();
	}
}
