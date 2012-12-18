package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.ComparisonTopic;

public class HibernateComparisonTopicDao extends HibernateAbstractEntityDao<ComparisonTopic, Long> implements
		ComparisonTopicDao
{
	private final TopicDao topicDao;

	@Inject
	public HibernateComparisonTopicDao(SessionFactory sessionFactory, TopicDao topicDao)
	{
		super(ComparisonTopic.class, sessionFactory);
		this.topicDao = topicDao;
	}

	@Override
	public void save(ComparisonTopic comparisonTopic)
	{
		topicDao.save(comparisonTopic.getTopic());
		getSession().persist(comparisonTopic);
	}
}
