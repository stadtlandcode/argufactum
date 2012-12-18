package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.services.ReferenceService;

public final class CreateReferenceForm extends AbstractAjaxForm<ReferenceService, Reference>
{
	@NotNull
	private Long factId;

	@Size(max = 255)
	private String text;

	public Long getFactId()
	{
		return factId;
	}

	public void setFactId(Long factId)
	{
		this.factId = factId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public Reference accept(ReferenceService service, Author author)
	{
		return service.create(this, author);
	}
}
