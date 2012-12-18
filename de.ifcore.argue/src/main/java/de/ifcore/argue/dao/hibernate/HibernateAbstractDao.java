package de.ifcore.argue.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateAbstractDao
{
	protected final SessionFactory sessionFactory;

	public HibernateAbstractDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}
}
