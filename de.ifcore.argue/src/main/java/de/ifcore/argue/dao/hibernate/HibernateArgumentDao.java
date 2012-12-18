package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentThesis;

public class HibernateArgumentDao extends HibernateAbstractEntityDao<Argument, Long> implements ArgumentDao
{
	@Inject
	public HibernateArgumentDao(SessionFactory sessionFactory)
	{
		super(Argument.class, sessionFactory);
	}

	@Override
	public void save(Argument argument)
	{
		ArgumentThesis thesis = argument.getArgumentThesis();
		argument.setArgumentThesis(null);
		super.save(argument);

		saveThesis(thesis);
		argument.setArgumentThesis(thesis);
		update(argument);
	}

	@Override
	public void saveThesis(ArgumentThesis thesis)
	{
		getSession().persist(thesis);
	}

	@Override
	public ArgumentThesis getThesis(Long id)
	{
		return (ArgumentThesis)getSession().get(ArgumentThesis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArgumentThesis> getTheses(Long argumentId)
	{
		Query query = getSession().createQuery("FROM ArgumentThesis WHERE argument.id = :argumentId");
		query.setParameter("argumentId", argumentId);
		return query.list();
	}
}
