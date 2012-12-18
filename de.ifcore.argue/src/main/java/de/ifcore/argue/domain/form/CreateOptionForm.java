package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.services.OptionService;

public class CreateOptionForm extends AbstractAjaxForm<OptionService, Option>
{
	@NotNull
	private Long topicId;

	@NotBlank
	private String label;

	private Long processId;

	@Override
	public Long getProcessId()
	{
		return processId;
	}

	public void setProcessId(Long processId)
	{
		this.processId = processId;
	}

	public Long getTopicId()
	{
		return topicId;
	}

	public void setTopicId(Long topicId)
	{
		this.topicId = topicId;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	@Override
	public Option accept(OptionService service, Author author)
	{
		return service.create(this, author);
	}

}
