package de.ifcore.argue.services;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicEntity;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.form.AddTopicAccessRightForm;
import de.ifcore.argue.domain.form.RemoveTopicAccessRightForm;
import de.ifcore.argue.domain.form.TopicVisibilityForm;
import de.ifcore.argue.domain.report.TopicAccessRightReport;

public class TopicAccessRightServiceImpl implements TopicAccessRightService
{
	private static final Logger log = Logger.getLogger(TopicAccessRightServiceImpl.class.getName());

	private final TopicDao topicDao;
	private final UserDao userDao;

	@Inject
	public TopicAccessRightServiceImpl(TopicDao topicDao, UserDao userDao)
	{
		this.topicDao = topicDao;
		this.userDao = userDao;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasAccess(TopicEntity topicEntity, RegisteredUser user, EntityPermission permission)
	{
		return hasAccess(topicEntity == null ? null : topicEntity.getTopicId(), user, permission);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasAccess(Long topicId, RegisteredUser user, EntityPermission permission)
	{
		if (topicId == null)
			return true;
		return Topic.isPermissionSufficient(permission, topicDao.get(topicId).getGrantedPermissionOfUser(user), user);
	}

	@Override
	@Transactional(readOnly = true)
	public void checkAccess(TopicEntity topicEntity, RegisteredUser user, EntityPermission permission)
	{
		checkAccess(topicEntity == null ? null : topicEntity.getTopicId(), user, permission);
	}

	@Override
	@Transactional(readOnly = true)
	public void checkAccess(Long topicId, RegisteredUser user, EntityPermission permission)
	{
		if (!hasAccess(topicId, user, permission))
			throw new AccessDeniedException("user " + ((user == null) ? "(null)" : user.getId())
					+ " has no access to topic " + topicId + " with permission " + permission);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean editedByMultipleUsers(Long topicId)
	{
		return getUserIdsWithWriteAccess(topicId).size() > 1;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Long> getUserIdsWithWriteAccess(Long topicId)
	{
		return topicDao.get(topicId).getUserIdsWithWriteAccess();
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<TopicAccessRightReport> getReport(Long topicId)
	{
		return new TreeSet<>(topicDao.get(topicId).getAccessRights());
	}

	@Override
	@Transactional
	public Topic setVisibility(TopicVisibilityForm form, Author author)
	{
		checkAccess(form.getId(), author.getRegisteredUser(), EntityPermission.WRITE);
		Topic topic = topicDao.get(form.getId());

		topic.setVisibility(form.getVisibility());
		topicDao.update(topic);
		log.info("set visibility of topic");
		return topic;
	}

	@Override
	@Transactional
	public TopicAccessRight shareTo(AddTopicAccessRightForm form, Author author)
	{
		checkAccess(form.getId(), author.getRegisteredUser(), EntityPermission.WRITE);
		Topic topic = topicDao.get(form.getId());
		RegisteredUser user = userDao.findByUsername(form.getContact());

		TopicAccessRight accessRight = new TopicAccessRight(topic, user, form.getPermission());
		topicDao.saveAccessRight(accessRight);
		log.info("share topic");
		return accessRight;
	}

	@Override
	@Transactional
	public TopicAccessRight removeAccessRight(RemoveTopicAccessRightForm form, Author author)
	{
		TopicAccessRight accessRight = topicDao.getAccessRight(form.getId());
		Topic topic = accessRight.getTopic();
		checkAccess(topic, author.getRegisteredUser(), EntityPermission.WRITE);

		if (!accessRight.isDeletable())
			throw new IllegalStateException();
		topicDao.deleteAccessRight(accessRight);
		log.info("remove access right");
		return accessRight;
	}
}
