package de.ifcore.argue.services;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.comparators.LogReportComparator;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.form.AddTopicVoteForm;
import de.ifcore.argue.domain.form.CreateTopicForm;
import de.ifcore.argue.domain.form.EditTopicForm;
import de.ifcore.argue.domain.form.RemoveTopicVoteForm;
import de.ifcore.argue.domain.report.TopicEventReport;

@Service
public class TopicServiceImpl extends AbstractTopicEntityService<Topic, TopicDao> implements TopicService
{
	private static final Logger log = Logger.getLogger(TopicServiceImpl.class.getName());

	private final CategoryDao categoryDao;
	private final DiscussionTypeServiceFactory discussionTypeServiceFactory;

	@Inject
	public TopicServiceImpl(TopicDao topicDao, TopicAccessRightService accessRightService, CategoryDao categoryDao,
			DiscussionTypeServiceFactory discussionTypeServiceFactory)
	{
		super(topicDao, accessRightService);
		this.categoryDao = categoryDao;
		this.discussionTypeServiceFactory = discussionTypeServiceFactory;
	}

	@Override
	@Transactional
	@RolesAllowed("ROLE_USER")
	public Topic create(CreateTopicForm form, Author author)
	{
		Topic topic = new Topic(form.getTerm(), form.getDefinition(), form.getDiscussionType(), author,
				form.getVisibility(), categoryDao.get(form.getCategoryId()));

		DiscussionTypeService<?> discussionTypeService = discussionTypeServiceFactory.getService(form
				.getDiscussionType());
		discussionTypeService.create(topic);
		return topic;
	}

	@Override
	@Transactional
	public Topic edit(EditTopicForm form, Author author)
	{
		Topic topic = entityDao.get(form.getId());
		accessRightService.checkAccess(topic, author.getRegisteredUser(), EntityPermission.WRITE);

		boolean persistEdit = topic.edit(form.getTerm(), form.getDefinition(), categoryDao.get(form.getCategoryId()),
				author);
		if (persistEdit)
		{
			entityDao.saveTerm(topic.getTerm());
			entityDao.update(topic);
			log.info("edit topic");
		}
		return topic;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<TopicEventReport> getEventReports(Long id, Author author)
	{
		accessRightService.checkAccess(entityDao.get(id), author.getRegisteredUser(), EntityPermission.WRITE);

		Set<TopicEventReport> reports = new TreeSet<>(LogReportComparator.getInstance());
		reports.addAll(entityDao.getTerms(id));
		log.info("get topic history");
		return reports;
	}

	@Override
	@Transactional
	public Topic copy(Long id, Author author)
	{
		Topic source = entityDao.get(id);
		accessRightService.checkAccess(source, author.getRegisteredUser(), EntityPermission.READ);

		Topic dest = source.copy(author);
		discussionTypeServiceFactory.getService(source.getDiscussionType()).saveWithDescendants(dest);
		log.info("copy topic");
		return dest;
	}

	@Override
	@Transactional
	public TopicVote addVote(AddTopicVoteForm form, Author author)
	{
		Topic topic = entityDao.get(form.getId());
		accessRightService.checkAccess(topic, author.getRegisteredUser(), EntityPermission.READ);

		TopicVote voteOfUser = topic.getTopicVoteOfUser(author.getUserId());
		if (voteOfUser == null)
		{
			voteOfUser = new TopicVote(form.isUpVote(), topic, author);
			entityDao.saveVote(voteOfUser);
		}
		else
		{
			voteOfUser.setVote(form.isUpVote());
			entityDao.updateVote(voteOfUser);
		}

		return voteOfUser;
	}

	@Override
	@Transactional
	public Topic removeVote(RemoveTopicVoteForm form, Author author)
	{
		Topic topic = entityDao.get(form.getId());
		accessRightService.checkAccess(topic, author.getRegisteredUser(), EntityPermission.READ);

		TopicVote voteOfUser = topic.getTopicVoteOfUser(author.getUserId());
		if (voteOfUser != null)
		{
			topic.removeVote(voteOfUser);
			entityDao.deleteVote(voteOfUser);
		}
		return topic;
	}
}
