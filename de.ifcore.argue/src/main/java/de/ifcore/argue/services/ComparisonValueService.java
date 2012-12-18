package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.form.ComparisonValueForm;

public interface ComparisonValueService extends Service
{
	public ComparisonValue persist(ComparisonValueForm form, Author author);
}
