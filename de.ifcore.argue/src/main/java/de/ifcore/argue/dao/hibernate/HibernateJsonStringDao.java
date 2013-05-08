package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.JsonStringDao;
import de.ifcore.argue.domain.entities.JsonString;

public class HibernateJsonStringDao extends HibernateAbstractEntityDao<JsonString, Long> implements JsonStringDao
{
	@Inject
	public HibernateJsonStringDao(SessionFactory sessionFactory)
	{
		super(JsonString.class, sessionFactory);
	}

	@Override
	public void save(JsonString object)
	{
		getSession().save(object);
	}
}
