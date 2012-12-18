package de.ifcore.argue.domain.models.view;

import java.util.Set;

public class AutocompletionModel implements ViewModel
{
	private static final long serialVersionUID = 673282081730696659L;
	private final Set<String> suggestions;

	public AutocompletionModel(Set<String> suggestions)
	{
		this.suggestions = suggestions;
	}

	public Set<String> getSuggestions()
	{
		return suggestions;
	}
}
