package de.ifcore.argue.domain.models;

import de.ifcore.argue.domain.entities.TopicEntity;

public class MockTopicEntity implements TopicEntity
{
	private static final long serialVersionUID = 2515170665425058564L;
	private final Long topicId;

	public MockTopicEntity(Long topicId)
	{
		this.topicId = topicId;
	}

	@Override
	public Long getTopicId()
	{
		return topicId;
	}

	@Override
	public Long getId()
	{
		return null;
	}
}
