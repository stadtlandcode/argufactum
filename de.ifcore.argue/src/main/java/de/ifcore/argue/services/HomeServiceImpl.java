package de.ifcore.argue.services;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.models.view.CategoryTopicsModel;
import de.ifcore.argue.domain.models.view.TopicModel;

@Service("homeService")
public class HomeServiceImpl implements HomeService
{
	private final TopicDao topicDao;
	private final CategoryDao categoryDao;
	private final MessageSource messageSource;

	private int numberOfTopicsPerSection = 5;
	private int minNumberOfMainEntities = 4;

	private Set<TopicModel> latestTopics;
	private Set<TopicModel> selectedTopics;
	private Set<CategoryTopicsModel> topicsByCategory;

	@Inject
	public HomeServiceImpl(TopicDao topicDao, CategoryDao categoryDao, MessageSource messageSource)
	{
		this.topicDao = topicDao;
		this.categoryDao = categoryDao;
		this.messageSource = messageSource;
	}

	@Override
	public Set<TopicModel> getLatestTopics()
	{
		if (latestTopics == null)
			updateLatestTopics();
		return latestTopics;
	}

	@Override
	public Set<TopicModel> getSelectedTopics()
	{
		if (selectedTopics == null)
			updateSelectedTopics();
		return selectedTopics;
	}

	@Override
	public Set<CategoryTopicsModel> getTopicsByCategory()
	{
		if (topicsByCategory == null)
			updateTopicsByCategory();
		return topicsByCategory;
	}

	@Override
	@Transactional(readOnly = true)
	public void updateLatestTopics()
	{
		Set<TopicModel> topics = new LinkedHashSet<>(numberOfTopicsPerSection);
		for (Topic topic : topicDao.getLatestPublicTopics(numberOfTopicsPerSection * 10))
		{
			addTopicToSet(topics, topic, numberOfTopicsPerSection, minNumberOfMainEntities, getSelectedTopics());
		}
		if (topics.size() < numberOfTopicsPerSection)
		{
			for (Topic topic : topicDao.getLatestPublicTopics(numberOfTopicsPerSection * 2))
			{
				addTopicToSet(topics, topic, numberOfTopicsPerSection, 0, getSelectedTopics());
			}
		}
		this.latestTopics = Collections.unmodifiableSet(topics);
	}

	@Override
	@Transactional(readOnly = true)
	public void updateSelectedTopics()
	{
		Set<TopicModel> topics = new LinkedHashSet<>(numberOfTopicsPerSection);
		for (Topic topic : topicDao.getSelectedTopics())
		{
			addTopicToSet(topics, topic, numberOfTopicsPerSection, 0, null);
		}
		this.selectedTopics = Collections.unmodifiableSet(topics);
	}

	@Override
	@Transactional(readOnly = true)
	public void updateTopicsByCategory()
	{
		Set<CategoryTopicsModel> categories = new LinkedHashSet<>();
		for (Category category : categoryDao.getAll())
		{
			SortedMap<Integer, Topic> topicPoints = new TreeMap<>(Collections.reverseOrder());
			for (Topic topic : category.getTopics())
			{
				if (TopicVisibility.PRIVATE.equals(topic.getVisibility()))
					continue;

				Integer points = topic.getNumberOfViews();
				if (topic.getDiscussionTypeReport().getNumberOfMainEntities().intValue() < 1)
				{
					points = Integer.valueOf(0);
				}

				if (topicPoints.size() < numberOfTopicsPerSection)
				{
					if (topicPoints.containsKey(points))
					{
						points = Integer.valueOf(topicPoints.firstKey().intValue() + 1);
					}
					topicPoints.put(points, topic);
				}
				else if (points.compareTo(topicPoints.lastKey()) > 0)
				{
					topicPoints.remove(topicPoints.lastKey());
					topicPoints.put(points, topic);
				}
			}

			if (topicPoints.isEmpty())
				continue;

			Set<TopicModel> topics = new LinkedHashSet<>(numberOfTopicsPerSection);
			for (Topic topic : topicPoints.values())
			{
				topics.add(new TopicModel(topic, messageSource));
			}
			categories.add(new CategoryTopicsModel(category, Collections.unmodifiableSet(topics)));
		}
		this.topicsByCategory = Collections.unmodifiableSet(categories);
	}

	private void addTopicToSet(Set<TopicModel> topics, Topic topic, int maxSize, int minNumberOfMainEntities,
			Set<TopicModel> exclude)
	{
		if (topics.size() < maxSize)
		{
			if (topic.getDiscussionTypeReport().getNumberOfMainEntities().intValue() >= minNumberOfMainEntities)
			{
				TopicModel topicModel = new TopicModel(topic, messageSource);
				if (exclude == null || !exclude.contains(topicModel))
				{
					topics.add(topicModel);
				}
			}
		}
	}

	public int getNumberOfTopicsPerSection()
	{
		return numberOfTopicsPerSection;
	}

	void setNumberOfTopicsPerSection(int numberOfTopicsPerSection)
	{
		this.numberOfTopicsPerSection = numberOfTopicsPerSection;
	}

	public int getMinNumberOfMainEntities()
	{
		return minNumberOfMainEntities;
	}

	void setMinNumberOfMainEntities(int minNumberOfMainEntities)
	{
		this.minNumberOfMainEntities = minNumberOfMainEntities;
	}
}
