package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "persistent_logins")
public class PersistentLogins
{
	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String series;

	@Column(nullable = false)
	@Id
	private String token;

	@Column(nullable = false, name = "last_used")
	private String lastUsed;

	public String getUsername()
	{
		return username;
	}

	public String getSeries()
	{
		return series;
	}

	public String getToken()
	{
		return token;
	}

	public String getLastUsed()
	{
		return lastUsed;
	}
}
