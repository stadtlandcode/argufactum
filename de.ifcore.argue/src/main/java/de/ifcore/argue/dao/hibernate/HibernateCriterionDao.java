package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.CriterionDao;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.CriterionImportance;

public class HibernateCriterionDao extends HibernateAbstractEntityDao<Criterion, Long> implements CriterionDao
{
	@Inject
	public HibernateCriterionDao(SessionFactory sessionFactory)
	{
		super(Criterion.class, sessionFactory);
	}

	@Override
	public CriterionImportance getImportance(Long id)
	{
		return (CriterionImportance)getSession().get(CriterionImportance.class, id);
	}

	@Override
	public void saveImportance(CriterionImportance importance)
	{
		getSession().persist(importance);
	}

	@Override
	public void updateImportance(CriterionImportance importance)
	{
		getSession().merge(importance);
	}
}
