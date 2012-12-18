package de.ifcore.argue.security;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.RegisteredUser;

@Service
public class FormLoginUserDetailsService implements UserDetailsService
{
	private final UserDao userDao;

	@Inject
	public FormLoginUserDetailsService(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
	{
		if (userName == null || userName.isEmpty())
			throw new IllegalArgumentException();

		RegisteredUser user;
		if (userName.contains("@"))
			user = userDao.findByEmail(userName);
		else
			user = userDao.findByUsername(userName);

		if (user == null)
			throw new UsernameNotFoundException(userName);
		return new ArgueUserDetailsImpl(user);
	}
}
