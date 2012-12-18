package de.ifcore.argue.services;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.validation.UsernameValidator;

@Service
public class UsernameServiceImpl implements UsernameService
{
	private final UserDao userDao;

	@Inject
	public UsernameServiceImpl(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	@Transactional(readOnly = true)
	public String guessByEmail(String email)
	{
		int indexOfAt = email.indexOf("@");
		if (indexOfAt < 1)
			throw new IllegalArgumentException();

		String username = email.substring(0, indexOfAt);
		return getUniqueUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public String guess(String username)
	{

		return getUniqueUsername(username);
	}

	private String getUniqueUsername(String baseUsername)
	{
		if (!UsernameValidator.getPattern().matcher(baseUsername).matches())
			baseUsername = baseUsername.replaceAll("[^A-Za-z0-9\\Q -.\\E]", "");
		if (!UsernameValidator.getPattern().matcher(baseUsername).matches())
			baseUsername = baseUsername.replaceAll("[^A-Za-z0-9]", "");
		if (baseUsername.length() < 3)
			baseUsername = "user-" + baseUsername;

		Set<String> takenUserNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		takenUserNames.addAll(userDao.findUsernamesStartingWith(baseUsername));
		if (takenUserNames.contains(baseUsername))
		{
			String usernamePrefix = baseUsername;
			int i = 1;
			while (takenUserNames.contains(baseUsername))
			{
				baseUsername = usernamePrefix + String.valueOf(i);
			}
		}
		return baseUsername;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getUsernamesStartingWith(String fragment)
	{
		return userDao.findUsernamesStartingWith(fragment);
	}
}
