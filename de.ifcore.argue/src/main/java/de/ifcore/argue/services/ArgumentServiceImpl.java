package de.ifcore.argue.services;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.ArgumentRelevanceVoteDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.domain.comparators.LogReportComparator;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentRelevanceVote;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.form.CreateArgumentForm;
import de.ifcore.argue.domain.form.EditArgumentForm;
import de.ifcore.argue.domain.report.EventReport;

@Service
public class ArgumentServiceImpl extends AbstractRelevanceVoteService<Argument, ArgumentDao, ArgumentRelevanceVote>
		implements ArgumentService
{
	private static final Logger log = Logger.getLogger(ArgumentServiceImpl.class.getName());
	private final ProContraTopicDao proContraTopicDao;

	@Inject
	public ArgumentServiceImpl(ArgumentDao argumentDao, ProContraTopicDao proContraTopicDao,
			TopicAccessRightService accessRightService, ArgumentRelevanceVoteDao relevanceVoteDao)
	{
		super(argumentDao, accessRightService, relevanceVoteDao);
		this.proContraTopicDao = proContraTopicDao;
	}

	@Override
	@Transactional
	public Argument create(CreateArgumentForm form, Author author)
	{
		ProContraTopic topic = proContraTopicDao.get(form.getTopicId());
		accessRightService.checkAccess(topic, author.getRegisteredUser(), EntityPermission.WRITE);

		Argument argument = new Argument(topic, form.getTopicThesis(), form.getThesis(), author);
		entityDao.save(argument);
		log.info("create argument");
		return argument;
	}

	@Override
	@Transactional
	public Argument edit(EditArgumentForm form, Author author)
	{
		Argument argument = entityDao.get(form.getId());
		accessRightService.checkAccess(argument, author.getRegisteredUser(), EntityPermission.WRITE);

		boolean persistEdit = argument.editThesis(form.getText(), author);
		if (persistEdit)
		{
			entityDao.saveThesis(argument.getArgumentThesis());
			entityDao.update(argument);
			log.info("edit argument");
		}
		return argument;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<EventReport> getEventReports(Long id, Author author)
	{
		Argument argument = entityDao.get(id);
		accessRightService.checkAccess(argument, author.getRegisteredUser(), EntityPermission.WRITE);

		Set<EventReport> reports = new TreeSet<>(LogReportComparator.getInstance());
		reports.addAll(argument.getLogs());
		reports.addAll(entityDao.getTheses(argument.getId()));
		log.info("get argument history");
		return Collections.unmodifiableSet(reports);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Fact> getDeletedFacts(Long argumentId, FactType factType, Author author)
	{
		Argument argument = entityDao.get(argumentId);
		accessRightService.checkAccess(argument, author.getRegisteredUser(), EntityPermission.WRITE);
		log.info("get deleted facts of argument");
		return argument.getDeletedFacts(factType);
	}

	@Override
	protected ArgumentRelevanceVote getNewRelevanceVote(Argument topicEntity, Relevance relevance, Author author)
	{
		return new ArgumentRelevanceVote(topicEntity, relevance, author);
	}
}
