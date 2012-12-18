package de.ifcore.argue.social;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import de.ifcore.argue.services.SecurityService;

public class SpringSecuritySignInAdapter implements SignInAdapter
{
	private final SecurityService securityService;

	@Inject
	public SpringSecuritySignInAdapter(SecurityService securityService)
	{
		this.securityService = securityService;
	}

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request)
	{
		securityService.autoLogin(Long.valueOf(userId));
		return null;
	}
}
