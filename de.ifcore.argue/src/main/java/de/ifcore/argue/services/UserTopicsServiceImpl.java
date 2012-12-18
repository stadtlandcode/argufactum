package de.ifcore.argue.services;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.models.view.TopicModel;

@Service
public class UserTopicsServiceImpl implements UserTopicsService
{
	private final TopicDao topicDao;
	private final MessageSource messageSource;

	@Inject
	public UserTopicsServiceImpl(TopicDao topicDao, MessageSource messageSource)
	{
		this.topicDao = topicDao;
		this.messageSource = messageSource;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<TopicModel> getAll(Long userId)
	{
		Set<TopicModel> topicModels = new LinkedHashSet<>();
		for (Topic topic : topicDao.getAssociatedTopics(userId))
		{
			topicModels.add(new TopicModel(topic, messageSource));
		}
		return Collections.unmodifiableSet(topicModels);
	}
}
