package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.services.ArgumentService;

public final class EditArgumentForm extends AbstractAjaxForm<ArgumentService, Argument>
{
	@NotNull
	private Long id;

	@Size(max = 255)
	@NotBlank
	private String text;

	@Override
	public Argument accept(ArgumentService service, Author author)
	{
		return service.edit(this, author);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
}
