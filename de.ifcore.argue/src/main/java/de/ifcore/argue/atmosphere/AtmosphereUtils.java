package de.ifcore.argue.atmosphere;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;

public class AtmosphereUtils
{
	public static AtmosphereResource getAtmosphereResource(HttpServletRequest request)
	{
		AtmosphereResource resource = (AtmosphereResource)request.getAttribute(AtmosphereResource.class.getName());
		if (resource == null)
			throw new NullPointerException("AtmosphereResource could not be located for the request");
		return resource;
	}
}
