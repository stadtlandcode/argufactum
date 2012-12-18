package de.ifcore.argue.security;

import java.util.UUID;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

import de.ifcore.argue.domain.entities.RegisteredOAuthConnection;
import de.ifcore.argue.domain.entities.RegisteredUser;

public class OAuthForeignAuthentication implements ForeignAuthentication
{
	private static final long serialVersionUID = -527698977993548101L;
	private final String email;
	private final String username;
	private final String personName;

	private final String providerId;
	private final String providerUserId;
	private final UUID uuid;

	public OAuthForeignAuthentication(Connection<?> connection)
	{
		UserProfile profile = connection.fetchUserProfile();
		if (profile != null)
		{
			this.email = profile.getEmail();
			this.username = profile.getUsername();
			this.personName = profile.getName();
		}
		else
		{
			this.email = null;
			this.username = null;
			this.personName = null;
		}
		this.providerId = connection.getKey().getProviderId();
		this.providerUserId = connection.getKey().getProviderUserId();
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void addToUser(RegisteredUser user)
	{
		new RegisteredOAuthConnection(user, providerId, providerUserId);
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	@Override
	public String getPersonName()
	{
		return personName;
	}

	@Override
	public UUID getUuid()
	{
		return uuid;
	}

	@Override
	public boolean isTrustable()
	{
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthForeignAuthentication [email=").append(email).append(", username=").append(username)
				.append(", personName=").append(personName).append(", providerId=").append(providerId)
				.append(", providerUserId=").append(providerUserId).append(", uuid=").append(uuid).append("]");
		return builder.toString();
	}
}
