package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentRelevanceVote;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.form.CreateArgumentForm;
import de.ifcore.argue.domain.form.EditArgumentForm;
import de.ifcore.argue.domain.report.EventReport;

public interface ArgumentService extends DeletableTopicEntityService<Argument>,
		RelevanceVoteService<ArgumentRelevanceVote>
{
	public Argument create(CreateArgumentForm form, Author author);

	public Argument edit(EditArgumentForm form, Author author);

	public Set<EventReport> getEventReports(Long id, Author author);

	public Set<Fact> getDeletedFacts(Long argumentId, FactType factType, Author author);
}
