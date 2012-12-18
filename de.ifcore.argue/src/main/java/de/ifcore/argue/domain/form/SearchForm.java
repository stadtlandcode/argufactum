package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.models.view.SearchResultModel;
import de.ifcore.argue.domain.validation.SearchTerm;
import de.ifcore.argue.services.SearchService;

// TODO limit
public class SearchForm extends AbstractForm<SearchService, SearchResultModel>
{
	@SearchTerm
	private String searchTerm;

	public String getSearchTerm()
	{
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	@Override
	public SearchResultModel accept(SearchService service, Author author)
	{
		return service.search(this);
	}
}
