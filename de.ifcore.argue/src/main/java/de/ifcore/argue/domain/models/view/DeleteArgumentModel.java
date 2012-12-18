package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.entities.DeletableTopicEntity;

public class DeleteArgumentModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -1718476004105810046L;

	public DeleteArgumentModel(DeletableTopicEntity entity)
	{
		super(entity, entity.getTopicId());
	}

	@Override
	public String getBroadcastSubject()
	{
		return "argument.delete";
	}
}
