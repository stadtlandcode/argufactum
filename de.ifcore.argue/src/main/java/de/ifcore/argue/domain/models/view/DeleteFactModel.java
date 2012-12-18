package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.entities.DeletableTopicEntity;

public class DeleteFactModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 1802898679364504949L;

	public DeleteFactModel(DeletableTopicEntity entity)
	{
		super(entity, entity.getTopicId());
	}

	@Override
	public String getBroadcastSubject()
	{
		return "fact.delete";
	}

}
