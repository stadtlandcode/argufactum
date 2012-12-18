package de.ifcore.argue.security;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.inject.internal.ImmutableList;

import de.ifcore.argue.domain.entities.RegisteredUser;

public class ArgueUserDetailsImpl implements ArgueUserDetails
{
	private static final long serialVersionUID = -1699734226669294970L;
	private static final List<SimpleGrantedAuthority> authorities = ImmutableList.of(new SimpleGrantedAuthority(
			"ROLE_USER"));
	private static final List<SimpleGrantedAuthority> adminAuthorities = ImmutableList.of(new SimpleGrantedAuthority(
			"ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));

	private final String username;
	private final String password;
	private final Long userId;
	private final String personName;
	private final DateTime createdAt;
	private final boolean admin;

	public ArgueUserDetailsImpl(RegisteredUser argueUser)
	{
		if (argueUser == null)
			throw new NullPointerException();
		this.username = argueUser.getUsername();
		this.password = argueUser.getPassword();
		this.userId = argueUser.getId();
		this.personName = argueUser.getPersonName();
		this.createdAt = argueUser.getCreatedAt();
		this.admin = argueUser.isAdmin();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return admin ? adminAuthorities : authorities;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	@Override
	public Long getUserId()
	{
		return userId;
	}

	@Override
	public String getPersonName()
	{
		return personName;
	}

	@Override
	public DateTime getCreatedAt()
	{
		return createdAt;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}
}
