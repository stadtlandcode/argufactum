package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "roac_uk", columnNames = { "providerid", "provideruserid" }) })
public final class RegisteredOAuthConnection implements IdEntity
{
	private static final long serialVersionUID = -2197170675583122459L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private RegisteredUser registeredUser;

	@Column(updatable = false, nullable = false)
	private String providerId;

	@Column(updatable = false, nullable = false)
	private String providerUserId;

	RegisteredOAuthConnection()
	{
	}

	public RegisteredOAuthConnection(RegisteredUser registeredUser, String providerId, String providerUserId)
	{
		if (registeredUser == null || providerId == null || providerUserId == null)
			throw new NullPointerException();
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.registeredUser = registeredUser;
		registeredUser.addOAuthConnection(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public RegisteredUser getRegisteredUser()
	{
		return registeredUser;
	}

	public String getProviderId()
	{
		return providerId;
	}

	public String getProviderUserId()
	{
		return providerUserId;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof RegisteredOAuthConnection))
			return false;

		final RegisteredOAuthConnection rhs = (RegisteredOAuthConnection)obj;
		return new EqualsBuilder().append(getProviderId(), rhs.getProviderId())
				.append(getProviderUserId(), rhs.getProviderUserId()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(3, 29).append(getProviderId()).append(getProviderUserId()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RegisteredOAuthConnection [id=").append(id).append(", providerId=").append(providerId)
				.append(", providerUserId=").append(providerUserId).append("]");
		return builder.toString();
	}
}
