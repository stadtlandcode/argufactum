package de.ifcore.argue.dao.hibernate;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.EntityDao;

public abstract class HibernateAbstractEntityDao<T, PK extends Serializable> extends HibernateAbstractDao implements
		EntityDao<T, PK>
{
	private final Class<T> type;

	public HibernateAbstractEntityDao(Class<T> type, SessionFactory sessionFactory)
	{
		super(sessionFactory);
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(PK id)
	{
		return id == null ? null : (T)getSession().get(type, id);
	}

	@Override
	public void save(T object)
	{
		getSession().persist(object);
	}

	@Override
	public void update(T object)
	{
		getSession().merge(object);
	}

	@Override
	public void delete(T object)
	{
		getSession().delete(object);
	}
}
