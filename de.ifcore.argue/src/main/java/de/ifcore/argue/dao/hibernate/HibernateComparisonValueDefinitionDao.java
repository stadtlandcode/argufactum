package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.domain.entities.ComparisonValueDefinition;
import de.ifcore.argue.domain.entities.ComparisonValueDefinition.ComparisonValueDefinitionPk;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;

public class HibernateComparisonValueDefinitionDao extends
		HibernateAbstractEntityDao<ComparisonValueDefinition, ComparisonValueDefinitionPk> implements
		ComparisonValueDefinitionDao
{
	@Inject
	public HibernateComparisonValueDefinitionDao(SessionFactory sessionFactory)
	{
		super(ComparisonValueDefinition.class, sessionFactory);
	}

	@Override
	public ComparisonValueDefinitionSet getSet(Long id)
	{
		return (ComparisonValueDefinitionSet)getSession().get(ComparisonValueDefinitionSet.class, id);
	}

	@Override
	public void saveSet(ComparisonValueDefinitionSet definitionSet)
	{
		getSession().persist(definitionSet);
		for (ComparisonValueDefinition definition : definitionSet.getDefinitions())
			getSession().persist(definition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComparisonValueDefinitionSet> getGlobalSets()
	{
		Query query = getSession().createQuery("FROM ComparisonValueDefinitionSet WHERE is_global = :is_global");
		query.setParameter("is_global", Boolean.TRUE);
		query.setCacheable(true);
		return query.list();
	}
}
