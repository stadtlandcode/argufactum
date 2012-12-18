package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.services.Service;

public abstract class AbstractIdAjaxForm<T extends Service, K> extends AbstractAjaxForm<T, K>
{
	@NotNull
	protected Long id;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
}
