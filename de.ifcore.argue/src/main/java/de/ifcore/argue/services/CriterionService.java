package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.CriterionImportance;
import de.ifcore.argue.domain.form.CreateCriterionForm;
import de.ifcore.argue.domain.form.CriterionImportanceForm;
import de.ifcore.argue.domain.form.DeleteCriterionForm;
import de.ifcore.argue.domain.form.EditCriterionForm;

public interface CriterionService extends Service
{
	public Criterion get(Long id);

	public Criterion create(CreateCriterionForm form, Author author);

	public Criterion edit(EditCriterionForm form, Author author);

	public Criterion delete(DeleteCriterionForm form, Author author);

	public CriterionImportance setImportance(CriterionImportanceForm form, Author author);
}
