package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.CreateOptionForm;
import de.ifcore.argue.domain.form.DeleteOptionForm;
import de.ifcore.argue.domain.form.EditOptionForm;

public interface OptionService extends Service
{
	public Option get(Long id);

	public Option create(CreateOptionForm form, Author author);

	public Option edit(EditOptionForm form, Author author);

	public Option delete(DeleteOptionForm form, Author author);
}
