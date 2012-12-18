package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.form.CreateReferenceForm;

public interface ReferenceService extends DeletableTopicEntityService<Reference>
{
	public Reference create(CreateReferenceForm form, Author author);
}
