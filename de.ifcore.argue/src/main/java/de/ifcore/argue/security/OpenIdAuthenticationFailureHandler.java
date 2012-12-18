package de.ifcore.argue.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class OpenIdAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
	public static final String OPEN_ID_FOREIGN_AUTHENTICATION_ATTRIBUTE = "openIdForeignAuthentication";
	private static final Logger log = Logger.getLogger(OpenIdAuthenticationFailureHandler.class.getName());
	private String defaultRegistrationUrl;
	private Set<String> trustedProviders;

	@SuppressWarnings("deprecation")
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException
	{
		if (isValidUnregisteredOpenId(exception))
		{
			OpenIDAuthenticationToken token = (OpenIDAuthenticationToken)exception.getAuthentication();
			ForeignAuthentication foreignAuthentication = new OpenIdForeignAuthentication(token,
					trustOpenIdProvider(token));
			request.getSession().setAttribute(OPEN_ID_FOREIGN_AUTHENTICATION_ATTRIBUTE, foreignAuthentication);
			log.trace("forwarding OpenId authenticated user to registration URL: " + foreignAuthentication);
			getRedirectStrategy().sendRedirect(request, response, defaultRegistrationUrl);
		}
		else
		{
			super.onAuthenticationFailure(request, response, exception);
		}
	}

	@SuppressWarnings("deprecation")
	private boolean isValidUnregisteredOpenId(AuthenticationException exception)
	{
		return exception instanceof UsernameNotFoundException
				&& exception.getAuthentication() instanceof OpenIDAuthenticationToken
				&& ((OpenIDAuthenticationToken)exception.getAuthentication()).getStatus().equals(
						OpenIDAuthenticationStatus.SUCCESS);
	}

	private boolean trustOpenIdProvider(OpenIDAuthenticationToken token)
	{
		boolean trusted = false;
		if (trustedProviders != null)
		{
			for (String prefix : trustedProviders)
			{
				if (token.getIdentityUrl().startsWith(prefix))
				{
					trusted = true;
					break;
				}
			}
		}
		return trusted;
	}

	public void setDefaultRegistrationUrl(String defaultRegistrationUrl)
	{
		this.defaultRegistrationUrl = defaultRegistrationUrl;
	}

	public void setTrustedProviders(Set<String> trustedProviders)
	{
		this.trustedProviders = Collections.unmodifiableSet(trustedProviders);
	}
}
