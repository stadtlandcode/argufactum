package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.FeedbackDao;
import de.ifcore.argue.domain.entities.Feedback;

public class HibernateFeedbackDao extends HibernateAbstractEntityDao<Feedback, Long> implements FeedbackDao
{
	@Inject
	public HibernateFeedbackDao(SessionFactory sessionFactory)
	{
		super(Feedback.class, sessionFactory);
	}
}
