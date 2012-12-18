package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.entities.TopicAccessRight;

public class RemoveAccessRightModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -8952370621589832994L;

	private final Long userId;

	public RemoveAccessRightModel(TopicAccessRight accessRight)
	{
		super(accessRight.getId(), accessRight.getTopic().getId());
		this.userId = accessRight.getUserId();
	}

	public Long getUserId()
	{
		return userId;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "topic.removeAccessRight";
	}
}
