package de.ifcore.argue.domain.models;

import java.io.Serializable;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.security.ArgueUserDetails;
import de.ifcore.argue.security.ForeignAuthentication;

public class UserSessionImpl implements UserSession, Serializable
{
	private static final long serialVersionUID = 5330733317842852407L;
	private transient UserDao userDao;

	private Long userId;
	private ForeignAuthentication foreignAuthentication;
	private PasswordStoreEntry storedPassword;

	@Override
	@Transactional(readOnly = true)
	public RegisteredUser getUser()
	{
		checkUser();
		return userId == null ? null : userDao.get(userId);
	}

	private synchronized void checkUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object userDetails = authentication == null ? null : authentication.getPrincipal();
		ArgueUserDetails argueUserDetails;
		if (userDetails != null && userDetails instanceof ArgueUserDetails)
			argueUserDetails = (ArgueUserDetails)userDetails;
		else
			argueUserDetails = null;

		if (argueUserDetails == null && userId != null)
		{
			userId = null;
		}
		else if (argueUserDetails != null && (userId == null || !userId.equals(argueUserDetails.getUserId())))
		{
			userId = argueUserDetails.getUserId();
		}
	}

	@Override
	public ForeignAuthentication getForeignAuthentication(String uuid)
	{
		if (foreignAuthentication == null || !foreignAuthentication.getUuid().equals(UUID.fromString(uuid)))
			return null;
		else
			return foreignAuthentication;
	}

	@Override
	public void setForeignAuthentication(ForeignAuthentication foreignAuthentication)
	{
		this.foreignAuthentication = foreignAuthentication;
	}

	@Override
	public String getStoredPassword(String uuid)
	{
		if (storedPassword != null && storedPassword.getUuid().equals(uuid))
			return storedPassword.getPassword();
		else
			return null;
	}

	@Override
	public void storePassword(String uuid, String encryptedPassword)
	{
		storedPassword = encryptedPassword == null ? null : new PasswordStoreEntry(uuid, encryptedPassword);
	}

	static class PasswordStoreEntry implements Serializable
	{
		private static final long serialVersionUID = 5795638863496574740L;
		private final String uuid;
		private final String password;

		PasswordStoreEntry(String uuid, String password)
		{
			if (password == null || uuid == null)
				throw new NullPointerException();
			this.password = password;
			this.uuid = uuid;
		}

		public String getPassword()
		{
			return password;
		}

		String getUuid()
		{
			return uuid;
		}
	}

	@Inject
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
}
