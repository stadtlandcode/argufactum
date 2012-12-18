package de.ifcore.argue.domain.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.services.Service;

@Component
public class FormHandlerImpl implements FormHandler
{
	private final FormStorage formStorage;

	@Autowired
	public FormHandlerImpl(FormStorage formStorage)
	{
		this.formStorage = formStorage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Service, K> K handleForm(Form<T, K> form, T service, Author author)
	{
		if (formStorage.isStored(form))
		{
			return (K)formStorage.getStoredEntityId(form);
		}
		else
		{
			K entity = form.accept(service, author);
			if (form.getProcessId() != null)
				formStorage.store(form, entity);
			return entity;
		}
	}
}
