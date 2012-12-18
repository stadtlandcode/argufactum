package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.controller.topic.TopicController;

public abstract class AbstractTopicBroadcast implements Broadcastable
{
	private static final long serialVersionUID = -3453989932444367993L;
	private final String responsibleBroadcasterId;

	protected AbstractTopicBroadcast(Long topicId)
	{
		this.responsibleBroadcasterId = TopicController.getBroadcasterId(topicId);
	}

	@Override
	public String responsibleBroadcasterId()
	{
		return responsibleBroadcasterId;
	}
}
