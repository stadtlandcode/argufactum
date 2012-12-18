package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ArgumentRelevanceVoteDao;
import de.ifcore.argue.domain.entities.ArgumentRelevanceVote;

public class HibernateArgumentRelevanceVoteDao extends HibernateAbstractEntityDao<ArgumentRelevanceVote, Long>
		implements ArgumentRelevanceVoteDao
{
	@Inject
	public HibernateArgumentRelevanceVoteDao(SessionFactory sessionFactory)
	{
		super(ArgumentRelevanceVote.class, sessionFactory);
	}
}
