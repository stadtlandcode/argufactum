package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.models.view.CategoryTopicsModel;
import de.ifcore.argue.domain.models.view.TopicModel;

public interface HomeService
{
	public Set<TopicModel> getLatestTopics();

	public Set<TopicModel> getSelectedTopics();

	public Set<CategoryTopicsModel> getTopicsByCategory();

	public void updateLatestTopics();

	public void updateSelectedTopics();

	public void updateTopicsByCategory();
}
