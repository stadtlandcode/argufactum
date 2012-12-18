package de.ifcore.argue.services;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.FactRelevanceVoteDao;
import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.domain.comparators.LogReportComparator;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.FactRelevanceVote;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.form.CreateFactForm;
import de.ifcore.argue.domain.form.EditFactForm;
import de.ifcore.argue.domain.report.EventReport;

@Service
public class FactServiceImpl extends AbstractRelevanceVoteService<Fact, FactDao, FactRelevanceVote> implements
		FactService
{
	private static final Logger log = Logger.getLogger(FactServiceImpl.class.getName());

	private final ArgumentDao argumentDao;
	private final ReferenceDao referenceDao;

	@Autowired
	public FactServiceImpl(FactDao factDao, ArgumentDao argumentDao, ReferenceDao referenceDao,
			TopicAccessRightService accessRightService, FactRelevanceVoteDao relevanceVoteDao)
	{
		super(factDao, accessRightService, relevanceVoteDao);
		this.argumentDao = argumentDao;
		this.referenceDao = referenceDao;
	}

	@Override
	@Transactional
	public Fact create(CreateFactForm form, Author author)
	{
		Argument argument = argumentDao.get(form.getArgumentId());
		accessRightService.checkAccess(argument, author.getRegisteredUser(), EntityPermission.WRITE);

		Fact fact = new Fact(argument, form.getFactType(), form.getText(), author);
		entityDao.save(fact);

		if (form.hasReference())
			referenceDao.save(Reference.instanceOf(form.getReference(), fact, author));

		log.info("create fact");
		return fact;
	}

	@Override
	@Transactional
	public Fact edit(EditFactForm form, Author author)
	{
		Fact fact = entityDao.get(form.getId());
		accessRightService.checkAccess(fact, author.getRegisteredUser(), EntityPermission.WRITE);

		boolean persistEdit = fact.editEvidence(form.getText(), author);
		if (persistEdit)
		{
			entityDao.saveEvidence(fact.getEvidence());
			entityDao.update(fact);
		}
		log.info("edit fact");
		return fact;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<EventReport> getEventReports(Long id, Author author)
	{
		Fact fact = entityDao.get(id);
		accessRightService.checkAccess(fact, author.getRegisteredUser(), EntityPermission.WRITE);

		Set<EventReport> reports = new TreeSet<>(LogReportComparator.getInstance());
		reports.addAll(fact.getLogs());
		reports.addAll(entityDao.getEvidences(fact.getId()));
		log.info("get fact history");
		return Collections.unmodifiableSet(reports);
	}

	@Override
	protected FactRelevanceVote getNewRelevanceVote(Fact topicEntity, Relevance relevance, Author author)
	{
		return new FactRelevanceVote(topicEntity, relevance, author);
	}
}
