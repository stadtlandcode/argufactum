package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.services.CriterionService;

public class EditCriterionForm extends AbstractAjaxForm<CriterionService, Criterion>
{
	@NotNull
	private Long id;

	private String label;

	private Long definitionSetId;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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
		return service.edit(this, author);
	}
}
