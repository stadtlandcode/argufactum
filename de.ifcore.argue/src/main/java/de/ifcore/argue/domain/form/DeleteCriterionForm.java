package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.services.CriterionService;

public class DeleteCriterionForm extends AbstractIdAjaxForm<CriterionService, Criterion>
{
	@Override
	public Criterion accept(CriterionService service, Author author)
	{
		return service.delete(this, author);
	}
}
