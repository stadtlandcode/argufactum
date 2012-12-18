package de.ifcore.argue.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
	private static final String ajaxUrlIdentifier = "/ajax";

	public AjaxAwareAuthenticationEntryPoint(String loginFormUrl)
	{
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException
	{
		if (request != null && request.getRequestURI() != null && request.getRequestURI().endsWith(ajaxUrlIdentifier))
		{
			response.sendError(901);
		}
		else
		{
			super.commence(request, response, authException);
		}
	}
}
