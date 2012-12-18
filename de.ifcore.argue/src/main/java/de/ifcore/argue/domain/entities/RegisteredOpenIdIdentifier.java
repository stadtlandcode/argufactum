package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public final class RegisteredOpenIdIdentifier implements IdEntity
{
	private static final long serialVersionUID = -1521290561300222243L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private RegisteredUser registeredUser;

	@Column(unique = true, updatable = false, nullable = false)
	private String identifier;

	RegisteredOpenIdIdentifier()
	{
	}

	public RegisteredOpenIdIdentifier(RegisteredUser registeredUser, String identifier)
	{
		if (registeredUser == null || identifier == null)
			throw new NullPointerException();
		this.identifier = identifier;
		this.registeredUser = registeredUser;
		registeredUser.addOpenIdIdentifier(this);
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

	public String getIdentifier()
	{
		return identifier;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof RegisteredOpenIdIdentifier))
			return false;

		final RegisteredOpenIdIdentifier rhs = (RegisteredOpenIdIdentifier)obj;
		return new EqualsBuilder().append(getIdentifier(), rhs.getIdentifier()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(19, 29).append(getIdentifier()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RegisteredOpenIdIdentifier [id=").append(id).append(", identifier=").append(identifier)
				.append("]");
		return builder.toString();
	}
}
