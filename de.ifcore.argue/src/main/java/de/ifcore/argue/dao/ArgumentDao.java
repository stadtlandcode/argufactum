package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentThesis;

public interface ArgumentDao extends EntityCruDao<Argument, Long>
{
	public ArgumentThesis getThesis(Long id);

	public void saveThesis(ArgumentThesis thesis);

	public List<ArgumentThesis> getTheses(Long argumentId);
}
