package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.FactRelevanceVoteDao;
import de.ifcore.argue.domain.entities.FactRelevanceVote;

public class HibernateFactRelevanceVoteDao extends HibernateAbstractEntityDao<FactRelevanceVote, Long> implements
		FactRelevanceVoteDao
{
	@Inject
	public HibernateFactRelevanceVoteDao(SessionFactory sessionFactory)
	{
		super(FactRelevanceVote.class, sessionFactory);
	}
}
