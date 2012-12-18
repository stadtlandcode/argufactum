package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.SearchDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class HibernateSearchDao extends HibernateAbstractDao implements SearchDao
{
	@Inject
	public HibernateSearchDao(SessionFactory sessionFactory)
	{
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> searchTopics(String searchTerm)
	{
		Query query = getSession()
				.createQuery(
						"SELECT t FROM Topic t INNER JOIN t.term tt WHERE (LOWER(tt.text) LIKE :searchTerm OR LOWER(tt.definition) LIKE :searchTerm) AND t.visibility = :visibility ORDER BY t.numberOfViews DESC");
		query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");
		query.setParameter("visibility", TopicVisibility.PUBLIC);
		return query.list();
	}
}
