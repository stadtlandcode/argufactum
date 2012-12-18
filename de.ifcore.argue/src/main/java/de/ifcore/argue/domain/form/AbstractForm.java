package de.ifcore.argue.domain.form;

import org.apache.commons.lang3.builder.ToStringBuilder;

import de.ifcore.argue.services.Service;

public abstract class AbstractForm<T extends Service, K> implements Form<T, K>
{
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Long getProcessId()
	{
		return null;
	}
}
