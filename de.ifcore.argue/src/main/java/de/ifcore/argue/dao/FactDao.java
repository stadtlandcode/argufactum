package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.FactEvidence;

public interface FactDao extends EntityDao<Fact, Long>
{
	public void saveEvidence(FactEvidence evidence);

	public List<FactEvidence> getEvidences(Long factId);
}
