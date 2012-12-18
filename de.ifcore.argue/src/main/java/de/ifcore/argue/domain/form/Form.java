package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.services.Service;

/**
 * Contains input fields of forms. Don't reuse an existing Form Object (or at least the processId) after calling the
 * accept() method (@see {@link Form#getProcessId()}
 * 
 * @author Felix Ebert
 * 
 */
public interface Form<T extends Service, K>
{
	public K accept(T service, Author author);

	/**
	 * id for current formular processing. a new unique id gets generated when the form is constructed and should be
	 * passed in as request parameter until you call the accept() method.
	 * 
	 * @return
	 */
	public Long getProcessId();
}
