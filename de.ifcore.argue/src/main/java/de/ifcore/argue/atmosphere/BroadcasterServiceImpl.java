package de.ifcore.argue.atmosphere;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.codehaus.jackson.map.ObjectMapper;

import de.ifcore.argue.domain.models.view.Broadcastable;

public class BroadcasterServiceImpl implements BroadcasterService
{
	private static final Logger log = Logger.getLogger(BroadcasterServiceImpl.class.getName());
	private final ObjectMapper mapper;
	private boolean broadcastingEnabled = true;
	private boolean acceptNewSubscriptions = true;
	private boolean acceptSubscriptions = true;

	public BroadcasterServiceImpl(ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	@Override
	public void subscribe(String broadcasterId, AtmosphereResource resource)
	{
		if (acceptSubscriptions)
		{
			Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(broadcasterId, acceptNewSubscriptions);
			if (broadcaster != null)
			{
				broadcaster.addAtmosphereResource(resource);
				resource.suspend();
				log.debug("resources of broadcaster " + broadcasterId + ": "
						+ broadcaster.getAtmosphereResources().size());
			}
		}
	}

	@Override
	public boolean broadcast(Broadcastable broadcastable, String resourceId)
	{
		if (broadcastable == null)
			throw new NullPointerException();

		if (broadcastingEnabled)
		{
			Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(broadcastable.responsibleBroadcasterId());
			if (broadcaster != null)
			{
				try
				{
					AtmosphereResource resourceToExclude = getResource(broadcastable.responsibleBroadcasterId(), resourceId);
					broadcaster.broadcast(mapper.writeValueAsString(broadcastable), resourceToExclude);
					log.trace("broadcasting " + broadcastable + " to all resources of broadcaster "
							+ broadcaster.getID() + ((resourceToExclude != null) ? " excluding one" : ""));
					return true;
				}
				catch (IOException e)
				{
					log.error("failed to broadcast " + broadcastable, e);
				}
			}
		}
		return false;
	}

	private AtmosphereResource getResource(String broadcasterId, String resourceId)
	{
		AtmosphereResource resource = null;
		if (resourceId != null)
		{
			Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(broadcasterId);
			if (broadcaster != null)
			{
				for (AtmosphereResource currentResource : broadcaster.getAtmosphereResources())
				{
					if (resourceId.equals(currentResource.getRequest().getParameter("resourceId")))
					{
						resource = currentResource;
						break;
					}
				}
			}
		}
		return resource;
	}

	@Override
	public boolean acceptsSubscription(String broadcasterId)
	{
		return acceptSubscriptions
				&& (acceptNewSubscriptions || BroadcasterFactory.getDefault().lookup(broadcasterId) != null);
	}

	public boolean isBroadcastingEnabled()
	{
		return broadcastingEnabled;
	}

	public void setBroadcastingEnabled(boolean broadcastingEnabled)
	{
		this.broadcastingEnabled = broadcastingEnabled;
	}

	public boolean isAcceptNewSubscriptions()
	{
		return acceptNewSubscriptions;
	}

	public void setAcceptNewSubscriptions(boolean acceptNewSubscriptions)
	{
		this.acceptNewSubscriptions = acceptNewSubscriptions;
	}

	public boolean isAcceptSubscriptions()
	{
		return acceptSubscriptions;
	}

	public void setAcceptSubscriptions(boolean acceptSubscriptions)
	{
		this.acceptSubscriptions = acceptSubscriptions;
	}
}
