package de.ifcore.argue.atmosphere;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResponse;

import de.ifcore.argue.controller.topic.TopicController;

public class AtmosphereSubscriptionHandler implements AtmosphereHandler
{
	private static final Logger log = Logger.getLogger(AtmosphereSubscriptionHandler.class.getName());

	@Override
	public void onRequest(AtmosphereResource r) throws IOException
	{
		AtmosphereRequest req = r.getRequest();

		if (req.getMethod().equalsIgnoreCase("GET"))
		{
			r.suspend();
			Long topicId = Long.valueOf(req.getParameter("topicId"));
			if (topicId == null)
				throw new IllegalArgumentException();
			String broadcasterId = TopicController.getBroadcasterId(topicId);
			BroadcasterServiceFactory.getService().subscribe(broadcasterId, r);
		}
		else
		{
			log.warn("recieved post request");
		}
	}

	@Override
	public void onStateChange(AtmosphereResourceEvent event) throws IOException
	{
		AtmosphereResource r = event.getResource();
		AtmosphereResponse res = r.getResponse();

		if (event.isSuspended())
		{
			res.getWriter().write(event.getMessage().toString());

			switch (r.transport())
			{
			case JSONP:
			case LONG_POLLING:
				event.getResource().resume();
				break;
			case WEBSOCKET:
			case STREAMING:
				res.getWriter().flush();
				break;
			default:
				log.error("unknow protocol " + r.transport());
			}
		}
	}

	@Override
	public void destroy()
	{
	}
}