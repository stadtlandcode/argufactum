package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.domain.entities.Option;

public class HibernateOptionDao extends HibernateAbstractEntityDao<Option, Long> implements OptionDao
{
	@Inject
	public HibernateOptionDao(SessionFactory sessionFactory)
	{
		super(Option.class, sessionFactory);
	}
}
