package de.ifcore.argue.domain.models;

import de.ifcore.argue.domain.entities.Author;

public interface UserRequest
{
	/**
	 * @return Author, never null
	 */
	public Author getAuthor();
}
