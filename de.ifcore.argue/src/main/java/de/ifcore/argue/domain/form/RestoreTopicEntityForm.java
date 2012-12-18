package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.services.DeletableTopicEntityService;

public class RestoreTopicEntityForm extends
		AbstractIdAjaxForm<DeletableTopicEntityService<? extends DeletableTopicEntity>, DeletableTopicEntity>
{
	@Override
	public DeletableTopicEntity accept(DeletableTopicEntityService<? extends DeletableTopicEntity> service,
			Author author)
	{
		return service.restore(this, author);
	}
}
