package de.ifcore.argue.domain.report;

import java.util.Set;

import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.entities.RegisteredUser;

public interface CriterionReport extends EntityReport
{
	public Long getTopicId();

	public String getLabel();

	public ComparisonValueDefinitionSet getDefinitionSet();

	public Byte getImportance(RegisteredUser user);

	public Set<ComparisonValueReport> getValues();
}
