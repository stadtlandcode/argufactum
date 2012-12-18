package de.ifcore.argue.security;

import java.io.Serializable;
import java.util.UUID;

import de.ifcore.argue.domain.entities.RegisteredUser;

public interface ForeignAuthentication extends Serializable
{
	public boolean isTrustable();

	public void addToUser(RegisteredUser user);

	public String getEmail();

	public String getUsername();

	public String getPersonName();

	public UUID getUuid();
}
