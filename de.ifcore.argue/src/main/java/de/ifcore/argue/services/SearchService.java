package de.ifcore.argue.services;

import de.ifcore.argue.domain.form.SearchForm;
import de.ifcore.argue.domain.models.view.SearchResultModel;

public interface SearchService extends Service
{
	public SearchResultModel search(SearchForm form);
}
