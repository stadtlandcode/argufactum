package de.ifcore.argue.domain.models.view;

import java.util.Set;
import java.util.SortedSet;

import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;
import de.ifcore.argue.domain.report.CriterionReport;

public class EditCriterionModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -3040694097860015992L;

	private final String label;
	private final Long definitionSetId;
	private final Set<ComparisonValueDefinitionSetModel> additionalDefinitionSets;

	public EditCriterionModel(CriterionReport criterion,
			SortedSet<ComparisonValueDefinitionSetReport> additionalDefinitionSets)
	{
		super(criterion, criterion.getTopicId());
		this.label = criterion.getLabel();
		this.definitionSetId = criterion.getDefinitionSet().getId();
		this.additionalDefinitionSets = ComparisonValueDefinitionSetModel.convert(additionalDefinitionSets);
	}

	@Override
	public String getBroadcastSubject()
	{
		return "editCriterion";
	}

	public String getLabel()
	{
		return label;
	}

	public Long getDefinitionSetId()
	{
		return definitionSetId;
	}

	/**
	 * @return all definitionSets of this criterion besides the global definitionSets
	 */
	public Set<ComparisonValueDefinitionSetModel> getAdditionalDefinitionSets()
	{
		return additionalDefinitionSets;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EditCriterionModel [label=").append(label).append(", definitionSetId=").append(definitionSetId)
				.append("]");
		return builder.toString();
	}
}
