package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.TopicEntity;

public interface TopicEntityService<T extends TopicEntity> extends EntityService<T>
{
	/**
	 * Gets the entity with the given id. Throws an exception if the given author has no access right to the topic.
	 * 
	 * @param id
	 * @param author
	 * @return
	 */
	public T get(Long id, Author author);
}
