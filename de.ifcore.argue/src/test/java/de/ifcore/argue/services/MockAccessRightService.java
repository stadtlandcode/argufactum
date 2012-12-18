package de.ifcore.argue.services;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

public class MockAccessRightService implements TopicAccessRightService
{
	@Override
	public boolean hasAccess(TopicEntity topicEntity, RegisteredUser user, EntityPermission permission)
	{
		return true;
	}

	@Override
	public boolean hasAccess(Long topicId, RegisteredUser user, EntityPermission permission)
	{
		return true;
	}

	@Override
	public void checkAccess(TopicEntity topicEntity, RegisteredUser registeredUser, EntityPermission permission)
	{
	}

	@Override
	public void checkAccess(Long topicId, RegisteredUser user, EntityPermission permission)
	{
	}

	@Override
	public boolean editedByMultipleUsers(Long topicId)
	{
		return false;
	}

	@Override
	public Set<Long> getUserIdsWithWriteAccess(Long topicId)
	{
		return Collections.emptySet();
	}

	@Override
	public SortedSet<TopicAccessRightReport> getReport(Long topicId)
	{
		return new TreeSet<>();
	}

	@Override
	public Topic setVisibility(TopicVisibilityForm topicVisibilityForm, Author author)
	{
		return null;
	}

	@Override
	public TopicAccessRight shareTo(AddTopicAccessRightForm form, Author author)
	{
		return null;
	}

	@Override
	public TopicAccessRight removeAccessRight(RemoveTopicAccessRightForm form, Author author)
	{
		return null;
	}
}
