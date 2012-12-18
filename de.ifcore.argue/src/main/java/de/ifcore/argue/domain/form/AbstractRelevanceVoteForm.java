package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.services.RelevanceVoteService;

public abstract class AbstractRelevanceVoteForm<T> extends AbstractIdAjaxForm<RelevanceVoteService<T>, T>
{
	private Relevance relevance;

	public Relevance getRelevance()
	{
		return relevance;
	}

	public void setRelevance(Relevance relevance)
	{
		this.relevance = relevance;
	}

	@Override
	public T accept(RelevanceVoteService<T> service, Author author)
	{
		return service.persistRelevanceVote(this, author);
	}
}
