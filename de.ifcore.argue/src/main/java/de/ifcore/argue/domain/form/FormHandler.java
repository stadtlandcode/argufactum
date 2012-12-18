package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.services.Service;

public interface FormHandler
{
	public <T extends Service, K> K handleForm(Form<T, K> form, T service, Author author);
}
