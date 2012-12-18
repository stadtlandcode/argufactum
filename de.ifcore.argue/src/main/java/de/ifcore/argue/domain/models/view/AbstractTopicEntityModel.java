package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.EntityReport;

public abstract class AbstractTopicEntityModel extends AbstractTopicBroadcast
{
	private static final long serialVersionUID = 8584453026589921251L;

	private final Long id;

	protected AbstractTopicEntityModel(EntityReport entity, Long topicId)
	{
		this(entity.getId(), topicId);
	}

	protected AbstractTopicEntityModel(Long id, Long topicId)
	{
		super(topicId);
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractTopicEntityModel [id=").append(id).append("]");
		return builder.toString();
	}
}
