package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.services.ProContraService;

public class EditProContraThesesForm extends AbstractAjaxForm<ProContraService, ProContraTopic>
{
	@NotNull
	private Long topicId;

	@NotNull
	private TopicThesesMessageCodeAppendix messageCodeAppendix;

	public Long getTopicId()
	{
		return topicId;
	}

	public void setTopicId(Long topicId)
	{
		this.topicId = topicId;
	}

	public TopicThesesMessageCodeAppendix getMessageCodeAppendix()
	{
		return messageCodeAppendix;
	}

	public void setMessageCodeAppendix(TopicThesesMessageCodeAppendix messageCodeAppendix)
	{
		this.messageCodeAppendix = messageCodeAppendix;
	}

	@Override
	public ProContraTopic accept(ProContraService service, Author author)
	{
		return service.editTheses(this, author);
	}
}
