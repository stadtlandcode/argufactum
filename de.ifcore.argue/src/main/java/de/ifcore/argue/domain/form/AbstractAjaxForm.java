package de.ifcore.argue.domain.form;

import de.ifcore.argue.services.Service;

public abstract class AbstractAjaxForm<T extends Service, K> extends AbstractForm<T, K>
{
	private String cometResourceId;

	public String getCometResourceId()
	{
		return cometResourceId;
	}

	public void setCometResourceId(String cometResourceId)
	{
		this.cometResourceId = cometResourceId;
	}
}
