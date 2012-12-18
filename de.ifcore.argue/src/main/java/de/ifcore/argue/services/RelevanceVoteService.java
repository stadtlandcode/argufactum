package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.form.AbstractRelevanceVoteForm;

public interface RelevanceVoteService<T> extends Service
{
	public <F extends AbstractRelevanceVoteForm<T>> T persistRelevanceVote(F form, Author author);
}
