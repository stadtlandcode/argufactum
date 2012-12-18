package de.ifcore.argue.social;

import java.util.UUID;

import de.ifcore.argue.domain.entities.RegisteredOpenIdIdentifier;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.security.ForeignAuthentication;

public class MockOpenIdForeignAuthentication implements ForeignAuthentication
{
	private static final long serialVersionUID = 4423439613917117703L;
	private String identifier;

	public MockOpenIdForeignAuthentication(String identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public void addToUser(RegisteredUser user)
	{
		new RegisteredOpenIdIdentifier(user, identifier);
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
