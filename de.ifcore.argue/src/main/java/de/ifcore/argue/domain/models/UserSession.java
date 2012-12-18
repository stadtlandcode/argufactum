package de.ifcore.argue.domain.models;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.security.ForeignAuthentication;

public interface UserSession
{
	public RegisteredUser getUser();

	public ForeignAuthentication getForeignAuthentication(String uuid);

	public void setForeignAuthentication(ForeignAuthentication foreignAuthentication);

	public String getStoredPassword(String uuid);

	public void storePassword(String uuid, String encryptedPassword);
}
