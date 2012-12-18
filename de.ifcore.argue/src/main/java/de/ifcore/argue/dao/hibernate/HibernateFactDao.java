package de.ifcore.argue.dao.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.FactEvidence;

public class HibernateFactDao extends HibernateAbstractEntityDao<Fact, Long> implements FactDao
{
	@Inject
	public HibernateFactDao(SessionFactory sessionFactory)
	{
		super(Fact.class, sessionFactory);
	}

	@Override
	public void save(Fact fact)
	{
		FactEvidence evidence = fact.getEvidence();
		fact.setEvidence(null);
		super.save(fact);

		saveEvidence(evidence);
		fact.setEvidence(evidence);
		update(fact);
	}

	@Override
	public void saveEvidence(FactEvidence evidence)
	{
		getSession().persist(evidence);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FactEvidence> getEvidences(Long factId)
	{
		Query query = getSession().createQuery("FROM FactEvidence WHERE fact.id = :factId");
		query.setParameter("factId", factId);
		return query.list();
	}
}
