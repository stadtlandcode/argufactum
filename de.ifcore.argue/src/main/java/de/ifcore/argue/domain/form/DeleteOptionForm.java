package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.services.OptionService;

public class DeleteOptionForm extends AbstractIdAjaxForm<OptionService, Option>
{
	@Override
	public Option accept(OptionService service, Author author)
	{
		return service.delete(this, author);
	}
}
