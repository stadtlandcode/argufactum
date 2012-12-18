package de.ifcore.argue.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;

@Service
public class ComparisonServiceImpl implements ComparisonService
{
	private final ComparisonTopicDao comparisonTopicDao;

	@Inject
	public ComparisonServiceImpl(ComparisonTopicDao comparisonTopicDao)
	{
		this.comparisonTopicDao = comparisonTopicDao;
	}

	@Override
	@Transactional
	public ComparisonTopic create(Topic topic)
	{
		ComparisonTopic comparisonTopic = new ComparisonTopic(topic);
		comparisonTopicDao.save(comparisonTopic);
		return comparisonTopic;
	}

	@Override
	public boolean canHandle(DiscussionType discussionType)
	{
		return DiscussionType.COMPARISON.equals(discussionType);
	}

	@Override
	@Transactional
	public void saveWithDescendants(Topic topic)
	{
		// TODO implement saveWithDescendants
	}
}
