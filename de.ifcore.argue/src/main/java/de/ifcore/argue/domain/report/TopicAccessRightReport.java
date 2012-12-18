package de.ifcore.argue.domain.report;

import java.io.Serializable;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.EntityPermission;

public interface TopicAccessRightReport extends Serializable, Comparable<TopicAccessRightReport>, EntityReport
{
	public Long getUserId();

	public RegisteredUser getUser();

	public String getContactName();

	public EntityPermission getPermission();

	public boolean isOwner();

	public boolean isDeletable();
}
