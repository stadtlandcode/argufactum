package de.ifcore.argue.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class AjaxAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private static final String ajaxUrlIdentifier = "/ajax";
	private final RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException
	{
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null && savedRequest.getRedirectUrl().contains(ajaxUrlIdentifier))
		{
			requestCache.removeRequest(request, response);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
