package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.FactRelevanceVote;
import de.ifcore.argue.domain.form.CreateFactForm;
import de.ifcore.argue.domain.form.EditFactForm;
import de.ifcore.argue.domain.report.EventReport;

public interface FactService extends DeletableTopicEntityService<Fact>, RelevanceVoteService<FactRelevanceVote>
{
	public Fact create(CreateFactForm form, Author author);

	public Fact edit(EditFactForm form, Author author);

	public Set<EventReport> getEventReports(Long id, Author author);
}
