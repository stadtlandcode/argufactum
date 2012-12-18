package de.ifcore.argue.domain.report;

import java.util.Set;

import de.ifcore.argue.domain.entities.AuthorAttribution;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.enumerations.FactType;

public interface FactReport extends DeletionReport, TopicEntityReport, RelevanceReport, AuthorAttribution
{
	public FactType getType();

	public LogReport getModificationLog();

	public LogReport getCreationLog();

	public Long getArgumentId();

	public Set<Reference> getReferences();

	public Long getEvidenceId();
}
