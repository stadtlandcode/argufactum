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
public class OpenIdUserDetailsService implements UserDetailsService
{
	private final UserDao userDao;

	@Inject
	public OpenIdUserDetailsService(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException
	{
		if (identifier == null || identifier.isEmpty())
			throw new IllegalArgumentException();

		RegisteredUser user = userDao.findByOpenIdIdentifier(identifier);
		if (user == null)
			throw new UsernameNotFoundException(identifier);
		return new ArgueUserDetailsImpl(user);
	}
}
