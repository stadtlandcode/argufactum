package de.ifcore.argue.dao;

import de.ifcore.argue.domain.entities.ProContraTopic;

public interface ProContraTopicDao extends EntityDao<ProContraTopic, Long>
{
	void saveWithDescendants(ProContraTopic proContraTopic);
}
