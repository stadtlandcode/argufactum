package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ComparisonValueDao;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.entities.ComparisonValue.ComparisonValuePk;

public class HibernateComparisonValueDao extends HibernateAbstractEntityDao<ComparisonValue, ComparisonValuePk>
		implements ComparisonValueDao
{
	@Inject
	public HibernateComparisonValueDao(SessionFactory sessionFactory)
	{
		super(ComparisonValue.class, sessionFactory);
	}

	@Override
	public ComparisonValue get(Long criterionId, Long optionId)
	{
		return get(new ComparisonValuePk(criterionId, optionId));
	}
}
