package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.services.FactService;

public class CreateFactForm extends AbstractAjaxForm<FactService, Fact>
{
	@NotNull
	private Long argumentId;

	@NotBlank
	@Size(max = 255)
	private String text;

	@NotNull
	private FactType factType;

	@Size(max = 255)
	private String reference;

	public Long getArgumentId()
	{
		return argumentId;
	}

	public void setArgumentId(Long argumentId)
	{
		this.argumentId = argumentId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public FactType getFactType()
	{
		return factType;
	}

	public void setFactType(FactType factType)
	{
		this.factType = factType;
	}

	@Override
	public Fact accept(FactService service, Author author)
	{
		return service.create(this, author);
	}

	public boolean hasReference()
	{
		return getReference() != null;
	}
}
