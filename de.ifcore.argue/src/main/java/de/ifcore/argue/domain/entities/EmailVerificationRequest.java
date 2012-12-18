package de.ifcore.argue.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

@Entity
public final class EmailVerificationRequest implements IdEntity
{
	private static final long serialVersionUID = 8114456237663740872L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false)
	private String email;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private RegisteredUser registeredUser;

	@Column(nullable = false, updatable = false, unique = true)
	private String verificationKey;

	@Column(nullable = false, updatable = false)
	private Date sentAt;

	private Date verifiedAt;

	@Column(nullable = false)
	private Boolean enabled;

	EmailVerificationRequest()
	{
	}

	public EmailVerificationRequest(RegisteredUser registeredUser)
	{
		if (registeredUser == null || registeredUser.getEmail() == null)
			throw new NullPointerException();

		this.email = registeredUser.getEmail();
		this.verificationKey = String.valueOf(System.currentTimeMillis()) + RandomStringUtils.randomNumeric(24);
		this.sentAt = new Date();
		this.enabled = Boolean.TRUE;
		this.registeredUser = registeredUser;
		registeredUser.addEmailVerificationRequest(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public String getEmail()
	{
		return email;
	}

	public RegisteredUser getRegisteredUser()
	{
		return registeredUser;
	}

	public String getVerificationKey()
	{
		return verificationKey;
	}

	public DateTime getSentAt()
	{
		return sentAt == null ? null : new DateTime(sentAt);
	}

	public DateTime getVerifiedAt()
	{
		return verifiedAt == null ? null : new DateTime(verifiedAt);
	}

	public boolean isEnabled()
	{
		return Boolean.TRUE.equals(enabled);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EmailVerificationRequest [id=").append(id).append(", email=").append(email)
				.append(", verificationKey=").append(verificationKey).append(", sentAt=").append(sentAt)
				.append(", verifiedAt=").append(verifiedAt).append(", enabled=").append(enabled).append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof EmailVerificationRequest))
			return false;

		final EmailVerificationRequest rhs = (EmailVerificationRequest)obj;
		return new EqualsBuilder().append(getVerificationKey(), rhs.getVerificationKey()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 47).append(getVerificationKey()).toHashCode();
	}

	public boolean isVerified()
	{
		return verifiedAt != null;
	}

	public void verify()
	{
		if (!isVerified())
		{
			verifiedAt = new Date();
			if (email.equals(registeredUser.getEmail()))
				registeredUser.setEmailVerified();
		}
	}
}
