package de.ifcore.argue.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public final class Author implements Serializable
{
	private static final long serialVersionUID = -3538562799641785320L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private RegisteredUser registeredUser;

	@Column(updatable = false)
	private String displayName;

	@Column(updatable = false)
	private String ip;

	Author()
	{
		this(null, null, null);
	}

	public Author(RegisteredUser registeredUser, String ip)
	{
		this(registeredUser, registeredUser.getUsername(), ip);
	}

	public Author(RegisteredUser registeredUser, String displayName, String ip)
	{
		this.registeredUser = registeredUser;
		this.displayName = displayName;
		this.ip = ip;
	}

	public RegisteredUser getRegisteredUser()
	{
		return registeredUser;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getIp()
	{
		return ip;
	}

	public Long getUserId()
	{
		return registeredUser == null ? null : registeredUser.getId();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Author))
			return false;

		final Author rhs = (Author)obj;
		return new EqualsBuilder().append(getDisplayName(), rhs.getDisplayName()).append(getIp(), rhs.getIp())
				.append(getUserId(), rhs.getUserId()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(5, 73).append(getDisplayName()).append(getIp()).append(getUserId()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Author [registeredUser=").append(registeredUser).append(", displayName=").append(displayName)
				.append(", ip=").append(ip).append("]");
		return builder.toString();
	}
}
