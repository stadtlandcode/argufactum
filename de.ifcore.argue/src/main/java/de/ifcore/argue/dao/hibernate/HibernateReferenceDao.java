package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.domain.entities.Reference;

public class HibernateReferenceDao extends HibernateAbstractEntityDao<Reference, Long> implements ReferenceDao
{
	@Inject
	public HibernateReferenceDao(SessionFactory sessionFactory)
	{
		super(Reference.class, sessionFactory);
	}
}
