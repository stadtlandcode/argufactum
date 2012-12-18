package de.ifcore.argue.domain.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.report.TopicAccessRightReport;

@Entity
public class TopicAccessRight implements TopicAccessRightReport, IdEntity
{
	private static final long serialVersionUID = 8742993276933750553L;

	private static SortedSet<EntityPermission> allowedPermissions;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private Topic topic;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private RegisteredUser registeredUser;

	@Column(nullable = false, updatable = false)
	private EntityPermission permission;

	@Column(nullable = false, updatable = false)
	private Date createdAt;

	@Column(nullable = false, updatable = false)
	private Boolean owner;

	TopicAccessRight()
	{
	}

	public TopicAccessRight(Topic topic, RegisteredUser user, EntityPermission permission)
	{
		if (topic == null || user == null || permission == null || !getAllowedPermissions().contains(permission))
			throw new IllegalArgumentException();
		this.topic = topic;
		this.registeredUser = user;
		this.permission = permission;
		this.owner = Boolean.valueOf(topic.getAccessRights().isEmpty());
		this.createdAt = new Date();
		topic.addAccessRight(this);
		user.addAccessRight(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public RegisteredUser getRegisteredUser()
	{
		return registeredUser;
	}

	@Override
	public EntityPermission getPermission()
	{
		return permission;
	}

	public DateTime getCreatedAt()
	{
		return createdAt == null ? null : new DateTime(createdAt);
	}

	@Override
	public boolean isOwner()
	{
		return BooleanUtils.toBoolean(owner);
	}

	public void setOwner(Boolean owner)
	{
		this.owner = owner;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof TopicAccessRight))
			return false;

		final TopicAccessRight rhs = (TopicAccessRight)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getUserId(), rhs.getUserId())
				.append(getPermission(), rhs.getPermission()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(19, 31).append(getCreatedAt()).append(getUserId()).append(getPermission())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TopicAccessRight [permission=").append(permission).append(", createdAt=").append(createdAt)
				.append("]");
		return builder.toString();
	}

	public static SortedSet<EntityPermission> getAllowedPermissions()
	{
		if (allowedPermissions == null)
		{
			allowedPermissions = Collections.unmodifiableSortedSet(new TreeSet<>(Arrays.asList(EntityPermission.READ,
					EntityPermission.WRITE)));
		}
		return allowedPermissions;
	}

	@Override
	public String getContactName()
	{
		return getRegisteredUser().getUsername();
	}

	@Override
	public Long getUserId()
	{
		return getRegisteredUser() == null ? null : getRegisteredUser().getId();
	}

	@Override
	public int compareTo(TopicAccessRightReport o)
	{
		if (this.equals(o))
			return 0;

		int result = getContactName().compareTo(o.getContactName());
		if (result == 0)
			result = getUserId().compareTo(o.getUserId());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	@Override
	public boolean isDeletable()
	{
		return !isOwner();
	}

	@Override
	public RegisteredUser getUser()
	{
		return getRegisteredUser();
	}
}
