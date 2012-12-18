package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.services.OptionService;

public class EditOptionForm extends AbstractAjaxForm<OptionService, Option>
{
	@NotNull
	private Long id;

	@NotBlank
	private String label;

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

	@Override
	public Option accept(OptionService service, Author author)
	{
		return service.edit(this, author);
	}

}
