package de.ifcore.argue.dao.hibernate;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Reference;

public class HibernateProContraTopicDao extends HibernateAbstractEntityDao<ProContraTopic, Long> implements
		ProContraTopicDao
{
	private final TopicDao topicDao;
	private final ArgumentDao argumentDao;
	private final FactDao factDao;
	private final ReferenceDao referenceDao;

	@Inject
	public HibernateProContraTopicDao(SessionFactory sessionFactory, TopicDao topicDao, ArgumentDao argumentDao,
			FactDao factDao, ReferenceDao referenceDao)
	{
		super(ProContraTopic.class, sessionFactory);
		this.topicDao = topicDao;
		this.argumentDao = argumentDao;
		this.factDao = factDao;
		this.referenceDao = referenceDao;
	}

	@Override
	public void save(ProContraTopic proContraTopic)
	{
		topicDao.save(proContraTopic.getTopic());
		getSession().persist(proContraTopic);
	}

	@Override
	public void saveWithDescendants(ProContraTopic proContraTopic)
	{
		save(proContraTopic);
		for (Argument argument : proContraTopic.getArguments())
		{
			argumentDao.save(argument);
			for (Fact fact : argument.getFacts())
			{
				factDao.save(fact);
				for (Reference reference : fact.getReferences())
				{
					referenceDao.save(reference);
				}
			}
		}
	}
}
