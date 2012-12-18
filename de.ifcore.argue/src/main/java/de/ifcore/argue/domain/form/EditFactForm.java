package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.services.FactService;

public class EditFactForm extends AbstractAjaxForm<FactService, Fact>
{
	@NotNull
	private Long id;

	@NotBlank
	@Size(max = 255)
	private String text;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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
	public Fact accept(FactService service, Author author)
	{
		return service.edit(this, author);
	}
}
