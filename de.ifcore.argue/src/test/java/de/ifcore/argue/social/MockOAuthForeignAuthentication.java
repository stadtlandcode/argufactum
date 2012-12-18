package de.ifcore.argue.social;

import java.util.UUID;

import de.ifcore.argue.domain.entities.RegisteredOAuthConnection;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.security.ForeignAuthentication;

public class MockOAuthForeignAuthentication implements ForeignAuthentication
{
	private static final long serialVersionUID = 8594510148990726924L;
	private String providerId;
	private String providerUserId;

	public MockOAuthForeignAuthentication(String providerId, String providerUserId)
	{
		this.providerId = providerId;
		this.providerUserId = providerUserId;
	}

	@Override
	public void addToUser(RegisteredUser user)
	{
		new RegisteredOAuthConnection(user, providerId, providerUserId);
	}

	@Override
	public String getEmail()
	{
		return null;
	}

	@Override
	public String getUsername()
	{
		return null;
	}

	@Override
	public String getPersonName()
	{
		return null;
	}

	@Override
	public UUID getUuid()
	{
		return null;
	}

	@Override
	public boolean isTrustable()
	{
		return false;
	}
}
