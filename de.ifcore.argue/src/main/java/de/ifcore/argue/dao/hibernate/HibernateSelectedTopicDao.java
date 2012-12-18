package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.SelectedTopicDao;
import de.ifcore.argue.domain.entities.SelectedTopic;

public class HibernateSelectedTopicDao extends HibernateAbstractEntityDao<SelectedTopic, Byte> implements
		SelectedTopicDao
{
	@Inject
	public HibernateSelectedTopicDao(SessionFactory sessionFactory)
	{
		super(SelectedTopic.class, sessionFactory);
	}
}
