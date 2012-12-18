package de.ifcore.argue.domain.report;

import java.util.Set;

import de.ifcore.argue.domain.entities.AuthorAttribution;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesis;

public interface ArgumentReport extends DeletionReport, TopicEntityReport, RelevanceReport, AuthorAttribution
{
	public Long getThesisId();

	public TopicThesis getTopicThesis();

	public LogReport getModificationLog();

	public LogReport getCreationLog();

	public Integer getNumberOfFacts(FactType confirmative);

	public Set<Fact> getFacts();
}
