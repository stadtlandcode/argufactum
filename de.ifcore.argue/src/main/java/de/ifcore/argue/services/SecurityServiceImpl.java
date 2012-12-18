package de.ifcore.argue.services;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.security.ArgueUserDetailsImpl;

@Service
public class SecurityServiceImpl implements SecurityService
{
	private final PasswordEncoder passwordEncoder;
	private final UserDao userDao;

	@Inject
	public SecurityServiceImpl(PasswordEncoder passwordEncoder, UserDao userDao)
	{
		this.passwordEncoder = passwordEncoder;
		this.userDao = userDao;
	}

	@Override
	public String encodePassword(String rawPassword)
	{
		return rawPassword == null ? null : passwordEncoder.encode(rawPassword);
	}

	@Override
	@Transactional(readOnly = true)
	public void autoLogin(Long userId)
	{
		UserDetails userDetails = new ArgueUserDetailsImpl(userDao.get(userId));
		Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
