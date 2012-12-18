package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import de.ifcore.argue.security.ForeignAuthentication;

@Entity
public final class RegisteredUser implements IdEntity
{
	private static final long serialVersionUID = 3544137217976801034L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String usernameNormalized;

	@Column
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String emailNormalized;

	private Boolean emailVerified;

	private String personName;

	@Column(updatable = false, nullable = false)
	private Date createdAt;

	@Column(updatable = false)
	private String ipAtRegistration;

	@Column(updatable = false)
	private Boolean admin;

	@OneToMany(mappedBy = "registeredUser", cascade = CascadeType.ALL)
	private Set<RegisteredOpenIdIdentifier> openIdIdentifiers;

	@OneToMany(mappedBy = "registeredUser", cascade = CascadeType.ALL)
	private Set<RegisteredOAuthConnection> oAuthConnections;

	@OneToMany(mappedBy = "registeredUser", cascade = CascadeType.REMOVE)
	private Set<EmailVerificationRequest> emailVerificationRequests;

	@OneToMany(mappedBy = "registeredUser", cascade = CascadeType.REMOVE)
	private Set<TopicAccessRight> accessRights;

	RegisteredUser()
	{
	}

	private RegisteredUser(String username, String email, String ip)
	{
		if (username == null || email == null)
			throw new NullPointerException();
		this.createdAt = new Date();
		this.ipAtRegistration = ip;
		this.username = username;
		this.usernameNormalized = formatForUsernameNormalized(username);
		this.email = formatForEmail(email);
		this.emailNormalized = formatForEmailNormalized(email);
	}

	public static String formatForUsernameNormalized(String username)
	{
		return username.toLowerCase();
	}

	public static String formatForEmail(String email)
	{
		return email.toLowerCase();
	}

	public static String formatForEmailNormalized(String email)
	{
		return StringUtils.remove(formatForEmail(email), "+");
	}

	public static RegisteredUser register(String username, String email, String password, String ip)
	{
		RegisteredUser registeredUser = new RegisteredUser(username, email, ip);
		if (password == null || password.isEmpty())
			throw new IllegalArgumentException();
		registeredUser.setPassword(password);
		return registeredUser;
	}

	public static RegisteredUser register(String username, String email, ForeignAuthentication authentication, String ip)
	{
		RegisteredUser registeredUser = new RegisteredUser(username, email, ip);
		authentication.addToUser(registeredUser);
		return registeredUser;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public String getUsername()
	{
		return username;
	}

	public String getUsernameNormalized()
	{
		return usernameNormalized;
	}

	public String getPassword()
	{
		return password;
	}

	private void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public String getEmailNormalized()
	{
		return emailNormalized;
	}

	public String getPersonName()
	{
		return personName;
	}

	public DateTime getCreatedAt()
	{
		return createdAt == null ? null : new DateTime(createdAt);
	}

	public String getIpAtRegistration()
	{
		return ipAtRegistration;
	}

	public Set<RegisteredOpenIdIdentifier> getOpenIdIdentifiers()
	{
		return openIdIdentifiers == null ? Collections.<RegisteredOpenIdIdentifier> emptySet() : Collections
				.unmodifiableSet(openIdIdentifiers);
	}

	void addOpenIdIdentifier(RegisteredOpenIdIdentifier identifier)
	{
		if (identifier == null || identifier.getRegisteredUser() != this)
			throw new IllegalArgumentException();
		if (openIdIdentifiers == null)
			openIdIdentifiers = new HashSet<>();
		openIdIdentifiers.add(identifier);
	}

	public Set<RegisteredOAuthConnection> getOAuthConnections()
	{
		return oAuthConnections == null ? Collections.<RegisteredOAuthConnection> emptySet() : Collections
				.unmodifiableSet(oAuthConnections);
	}

	void addOAuthConnection(RegisteredOAuthConnection connection)
	{
		if (connection == null || connection.getRegisteredUser() != this)
			throw new IllegalArgumentException();
		if (oAuthConnections == null)
			oAuthConnections = new HashSet<>();
		oAuthConnections.add(connection);
	}

	public Set<EmailVerificationRequest> getEmailVerificationRequests()
	{
		return emailVerificationRequests == null ? Collections.<EmailVerificationRequest> emptySet() : Collections
				.unmodifiableSet(emailVerificationRequests);
	}

	void addEmailVerificationRequest(EmailVerificationRequest request)
	{
		if (request == null || request.getRegisteredUser() != this)
			throw new IllegalArgumentException();
		if (emailVerificationRequests == null)
			emailVerificationRequests = new HashSet<>();
		emailVerificationRequests.add(request);
	}

	public boolean isEmailVerified()
	{
		return Boolean.TRUE.equals(emailVerified);
	}

	public void setEmailVerified()
	{
		emailVerified = Boolean.TRUE;
	}

	public Set<TopicAccessRight> getAccessRights()
	{
		return accessRights == null ? Collections.<TopicAccessRight> emptySet() : Collections
				.unmodifiableSet(accessRights);
	}

	void addAccessRight(TopicAccessRight accessRight)
	{
		if (accessRight == null || accessRight.getRegisteredUser() != this)
			throw new IllegalArgumentException();
		if (this.accessRights == null)
			this.accessRights = new HashSet<>();
		this.accessRights.add(accessRight);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof RegisteredUser))
			return false;

		final RegisteredUser rhs = (RegisteredUser)obj;
		return new EqualsBuilder().append(getUsername(), rhs.getUsername())
				.append(getPersonName(), rhs.getPersonName()).append(getEmail(), rhs.getEmail())
				.append(getCreatedAt(), rhs.getCreatedAt()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 47).append(getUsername()).append(getPersonName()).append(getEmail())
				.append(getCreatedAt()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RegisteredUser [id=").append(id).append(", username=").append(username).append(", email=")
				.append(email).append(", emailVerified=").append(emailVerified).append(", personName=")
				.append(personName).append(", createdAt=").append(createdAt).append("]");
		return builder.toString();
	}

	public boolean isAdmin()
	{
		return Boolean.TRUE.equals(admin);
	}
}
