package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.entities.Topic;

public class SearchResultModel implements ViewModel
{
	private static final long serialVersionUID = -3653631329243026113L;

	private final String searchTerm;
	private final Set<TopicModel> topics;

	public SearchResultModel(String searchTerm, List<Topic> topics, MessageSource messageSource)
	{
		this.searchTerm = searchTerm;
		Set<TopicModel> topicModels = new LinkedHashSet<>();
		for (Topic topic : topics)
			topicModels.add(new TopicModel(topic, messageSource));
		this.topics = Collections.unmodifiableSet(topicModels);
	}

	public String getSearchTerm()
	{
		return searchTerm;
	}

	public Set<TopicModel> getTopics()
	{
		return topics;
	}
}
