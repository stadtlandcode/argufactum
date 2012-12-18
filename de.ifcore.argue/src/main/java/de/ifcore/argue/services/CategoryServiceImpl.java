package de.ifcore.argue.services;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

@Service
public class CategoryServiceImpl extends AbstractEntityService<Category, CategoryDao> implements CategoryService
{
	@Inject
	public CategoryServiceImpl(CategoryDao categoryDao)
	{
		super(categoryDao);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Category> getAll()
	{
		return new TreeSet<>(entityDao.getAll());
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Topic> getPublicTopics(Long categoryId)
	{
		Category category = entityDao.get(categoryId);
		Set<Topic> topics = new LinkedHashSet<>();
		for (Topic topic : category.getTopics())
		{
			if (TopicVisibility.PUBLIC.equals(topic.getVisibility()))
			{
				topics.add(topic);
			}
		}
		return Collections.unmodifiableSet(topics);
	}
}
