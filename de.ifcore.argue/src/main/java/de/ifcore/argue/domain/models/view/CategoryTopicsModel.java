package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.Set;

import de.ifcore.argue.domain.entities.Category;

public class CategoryTopicsModel extends CategoryModel
{
	private static final long serialVersionUID = 1204684899062104114L;

	private final Integer totalNumberOfTopics;
	private final Set<TopicModel> topics;

	public CategoryTopicsModel(Category category, Set<TopicModel> topics)
	{
		super(category);
		this.totalNumberOfTopics = category.getNumberOfTopics();
		this.topics = Collections.unmodifiableSet(topics);
	}

	public Integer getTotalNumberOfTopics()
	{
		return totalNumberOfTopics;
	}

	public Set<TopicModel> getTopics()
	{
		return topics;
	}
}
