package de.ifcore.argue.domain.models;

import de.ifcore.argue.domain.entities.RegisteredUser;

public class MockUserSession extends UserSessionImpl
{
	private static final long serialVersionUID = -5145496696028016479L;

	private RegisteredUser user;

	public MockUserSession()
	{
	}

	public MockUserSession(RegisteredUser user)
	{
		this();
		this.user = user;
	}

	@Override
	public RegisteredUser getUser()
	{
		return user;
	}

	public void setUser(RegisteredUser user)
	{
		this.user = user;
	}
}
