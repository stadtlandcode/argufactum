package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.form.DeleteTopicEntityForm;
import de.ifcore.argue.domain.form.RestoreTopicEntityForm;

public interface DeletableTopicEntityService<T extends DeletableTopicEntity> extends TopicEntityService<T>
{
	public T delete(DeleteTopicEntityForm form, Author author);

	public T restore(RestoreTopicEntityForm form, Author author);
}
