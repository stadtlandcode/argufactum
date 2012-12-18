package de.ifcore.argue.dao;

import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.CriterionImportance;

public interface CriterionDao extends EntityCruDao<Criterion, Long>
{
	public CriterionImportance getImportance(Long id);

	public void saveImportance(CriterionImportance importance);

	public void updateImportance(CriterionImportance importance);
}
