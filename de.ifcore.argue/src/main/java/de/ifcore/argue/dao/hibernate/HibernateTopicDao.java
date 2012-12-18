package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicTerm;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class HibernateTopicDao extends HibernateAbstractEntityDao<Topic, Long> implements TopicDao
{
	@Inject
	public HibernateTopicDao(SessionFactory sessionFactory)
	{
		super(Topic.class, sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> getLatestPublicTopics(int maxResults)
	{
		Query query = getSession().createQuery("FROM Topic WHERE visibility = :visibility ORDER BY createdAt DESC");
		query.setMaxResults(maxResults);
		query.setCacheable(true);
		query.setParameter("visibility", TopicVisibility.PUBLIC);
		return query.list();
	}

	@Override
	public void save(Topic topic)
	{
		if (topic.getId() == null)
		{
			TopicTerm term = topic.getTerm();
			topic.setTerm(null);
			super.save(topic);

			saveTerm(term);
			topic.setTerm(term);
			update(topic);
		}
		else
		{
			super.save(topic);
		}
	}

	@Override
	public void saveTerm(TopicTerm topicTerm)
	{
		getSession().persist(topicTerm);
	}

	@Override
	public void saveAccessRight(TopicAccessRight accessRight)
	{
		getSession().persist(accessRight);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TopicTerm> getTerms(Long topicId)
	{
		Query query = getSession().createQuery("FROM TopicTerm WHERE topic.id = :topicId");
		query.setParameter("topicId", topicId);
		return query.list();
	}

	@Override
	public TopicAccessRight getAccessRight(Long id)
	{
		return (TopicAccessRight)getSession().get(TopicAccessRight.class, id);
	}

	@Override
	public void deleteAccessRight(TopicAccessRight accessRight)
	{
		getSession().delete(accessRight);
	}

	@Override
	public void saveVote(TopicVote vote)
	{
		getSession().persist(vote);
	}

	@Override
	public void updateVote(TopicVote vote)
	{
		getSession().merge(vote);
	}

	@Override
	public void deleteVote(TopicVote vote)
	{
		getSession().delete(vote);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> getSelectedTopics()
	{
		Query query = getSession().createQuery("SELECT topic FROM SelectedTopic ORDER BY position ASC");
		query.setCacheable(true);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> getAssociatedTopics(Long userId)
	{
		Query query = getSession()
				.createQuery(
						"SELECT tar.topic FROM TopicAccessRight tar WHERE tar.registeredUser.id = :userId ORDER BY tar.createdAt DESC");
		query.setParameter("userId", userId);
		query.setCacheable(true);
		return query.list();
	}
}
