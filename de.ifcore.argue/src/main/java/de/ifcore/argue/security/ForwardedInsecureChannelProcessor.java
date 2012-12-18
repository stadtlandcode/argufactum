package de.ifcore.argue.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.channel.InsecureChannelProcessor;

public class ForwardedInsecureChannelProcessor extends InsecureChannelProcessor
{

	@Override
	public void decide(FilterInvocation invocation, Collection<ConfigAttribute> config) throws IOException,
			ServletException
	{
		if ((invocation == null) || (config == null))
		{
			throw new IllegalArgumentException("Nulls cannot be provided");
		}

		for (ConfigAttribute attribute : config)
		{
			if (supports(attribute))
			{
				if (invocation.getHttpRequest().isSecure()
						|| "https".equals(invocation.getHttpRequest().getHeader("X-Forwarded-Proto")))
				{
					getEntryPoint().commence(invocation.getRequest(), invocation.getResponse());
				}
			}
		}
	}

}
