package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.entities.DeletableTopicEntity;

public class DeleteReferenceModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 9161323523111490817L;

	public DeleteReferenceModel(DeletableTopicEntity entity)
	{
		super(entity, entity.getTopicId());
	}

	@Override
	public String getBroadcastSubject()
	{
		return "reference.delete";
	}
}
