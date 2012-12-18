package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.services.CriterionService;

public class CreateCriterionForm extends AbstractAjaxForm<CriterionService, Criterion>
{
	@NotNull
	private Long topicId;

	@NotBlank
	private String label;

	@NotNull
	private Long definitionSetId;

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

	public Long getDefinitionSetId()
	{
		return definitionSetId;
	}

	public void setDefinitionSetId(Long definitionSetId)
	{
		this.definitionSetId = definitionSetId;
	}

	@Override
	public Criterion accept(CriterionService service, Author author)
	{
		return service.create(this, author);
	}
}
