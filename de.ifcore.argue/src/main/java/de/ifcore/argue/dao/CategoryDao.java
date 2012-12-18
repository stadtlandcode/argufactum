package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.Category;

public interface CategoryDao extends EntityDao<Category, Long>
{
	public List<Category> getAll();

	public Category findByName(String name);
}
