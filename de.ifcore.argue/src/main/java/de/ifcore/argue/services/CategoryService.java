package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;

public interface CategoryService extends EntityService<Category>
{
	/**
	 * @return Sorted Set containing all categories
	 */
	public Set<Category> getAll();

	public Set<Topic> getPublicTopics(Long categoryId);
}
