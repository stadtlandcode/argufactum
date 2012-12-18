package de.ifcore.argue.atmosphere;

import org.atmosphere.cpr.AtmosphereResource;

import de.ifcore.argue.domain.models.view.Broadcastable;

public interface BroadcasterService
{
	/**
	 * suspends and subscribes the Atmosphereresource of the given request to a broadcaster, creates a new broadcaster
	 * if none exists with given broadcasterId
	 * 
	 * @param broadcasterId
	 * @param resource
	 */
	public void subscribe(String broadcasterId, AtmosphereResource resource);

	/**
	 * broadcasts the broadcastable object to all resources of given broadcaster excluding the resource with the given
	 * resourceId. exits silently when there isn't a broadcaster for the given broadcasterId or if the broadcaster has
	 * only one resource.
	 * 
	 * @param broadcastable
	 * @param resourceId
	 * @return true when the object got broadcasted
	 */
	public boolean broadcast(Broadcastable broadcastable, String resourceId);

	/**
	 * @param broadcasterId
	 * @return true when this service will presumably accept a subscription to given broadcasterid
	 */
	public boolean acceptsSubscription(String broadcasterId);
}