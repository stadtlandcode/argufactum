package de.ifcore.argue.domain.entities;

import java.util.Set;

public interface AuthorAttribution
{
	/**
	 * @return true: offer the link to display the attributions
	 */
	public boolean offerAttributionList();

	/**
	 * @return List of all authors
	 */
	public Set<String> getAuthorList();
}
