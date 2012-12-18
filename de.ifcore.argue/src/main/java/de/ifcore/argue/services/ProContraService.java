package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.form.EditProContraThesesForm;

public interface ProContraService extends DiscussionTypeService<ProContraTopic>
{
	public ProContraTopic editTheses(EditProContraThesesForm form, Author author);

	public Set<Argument> getDeletedArguments(Long topicId, TopicThesis topicThesis, Author author);
}
