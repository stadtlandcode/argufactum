package de.ifcore.argue.atmosphere;

import org.codehaus.jackson.map.ObjectMapper;

public class BroadcasterServiceFactory
{
	private static final BroadcasterService service = new BroadcasterServiceImpl(new ObjectMapper());

	public static BroadcasterService getService()
	{
		return service;
	}
}
