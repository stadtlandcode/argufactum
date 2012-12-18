package de.ifcore.argue.domain.models.view;

import java.io.Serializable;

public interface Broadcastable extends Serializable
{
	public String getBroadcastSubject();

	public String responsibleBroadcasterId();
}
