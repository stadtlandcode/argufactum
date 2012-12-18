package de.ifcore.argue.services;

import java.util.Set;
import java.util.SortedSet;

import org.springframework.security.access.AccessDeniedException;

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

public interface TopicAccessRightService extends Service
{
	public boolean hasAccess(TopicEntity topicEntity, RegisteredUser user, EntityPermission permission);

	public boolean hasAccess(Long topicId, RegisteredUser user, EntityPermission permission);

	/**
	 * @param topicEntity
	 * @param user
	 * @param permission
	 * @throws AccessDeniedException
	 *             when user hasn't access with given permission to topicEntity
	 */
	public void checkAccess(TopicEntity topicEntity, RegisteredUser user, EntityPermission permission);

	/**
	 * @see TopicAccessRightService#checkAccess(TopicEntity, RegisteredUser, EntityPermission)
	 */
	public void checkAccess(Long topicId, RegisteredUser user, EntityPermission permission);

	/**
	 * @param topicId
	 * @return true when multiple users have write access to given topic
	 */
	public boolean editedByMultipleUsers(Long topicId);

	public Set<Long> getUserIdsWithWriteAccess(Long topicId);

	public SortedSet<TopicAccessRightReport> getReport(Long topicId);

	public Topic setVisibility(TopicVisibilityForm form, Author author);

	public TopicAccessRight shareTo(AddTopicAccessRightForm form, Author author);

	public TopicAccessRight removeAccessRight(RemoveTopicAccessRightForm form, Author author);
}
