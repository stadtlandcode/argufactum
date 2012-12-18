package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.domain.entities.Category;

public class HibernateCategoryDao extends HibernateAbstractEntityDao<Category, Long> implements CategoryDao
{
	@Inject
	public HibernateCategoryDao(SessionFactory sessionFactory)
	{
		super(Category.class, sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getAll()
	{
		Query query = getSession().createQuery("FROM Category");
		query.setCacheable(true);
		return query.list();
	}

	@Override
	public Category findByName(String name)
	{
		Query query = getSession().createQuery("FROM Category WHERE name=:name");
		query.setParameter("name", name);
		return (Category)query.uniqueResult();
	}

	@Override
	public void delete(Category category)
	{
		if (!category.getTopics().isEmpty())
			throw new UnsupportedOperationException();
		super.delete(category);
	}
}
